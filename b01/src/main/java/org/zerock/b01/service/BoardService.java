package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    // 게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    /*
     * 그동안 dto <-> vo(domain)을 변환할 때는 ModelMapper 객체를 사용했지만
     * 이 경우에는 자동 변환하는 설정이 어려워서(???) ModelMapper를 사용하지 않고,
     * 직접 변환하는 메서드를 만들었다.
     * */
    /*
    default 메서드는 interface에 구현메서드를 추가할 수 있는 문법이다.
    원래 객체지향에는 없는 개념이었다.
    왜냐하면 inteface는 추상메서드만 존재하고
    abstract 클래스가 구현메서드 + 추상메서드
    일반 구현 클래스는 모두 구현메서드만 존재하는 것이 원칙이었다.
    그런데 실제 프레임워크/라이브러리의 Version을 업데이트하고 새로운 기능을 추가할 때
    기존에 사용하던 인터페이스에 새로운 추상 메서드를 추가하는 순간
    기존에 사용하던 모든 소스에서 에러가 발생되기 때문에
    부득이하게 인터페이스에 구현메서드를 추가하는 문법을 새로 만들었다.
    default메서드는 반드시 자식 구현 객체에서만 사용할 수 있다.
    * */
    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if (boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    default BoardDTO entityToDTO(Board board){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSet().stream().sorted().map(boardImage ->
                    boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}