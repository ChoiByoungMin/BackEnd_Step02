package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardImageDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

// 반드시 상속받은 인터페이스명 + Impl
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    // 우리는 BoardRepostitory 내부에서 호출할 메서드를
    // QueryDsl로 구현한다.
    @Override
    public Page<Board> search1(Pageable pageable) {

        // 우리가 작업하는 querydsl 프로그램 -> jpql로 변환하기 위해 사용
        QBoard board = QBoard.board; // q도메인 객체

        /*
        * querydsl을 통해서 1단계씩 sql문을 작성해 나간다.
        * */

        // from BOARD
        JPQLQuery<Board> query = from(board); // select.. from board

//        // WHERE title LIKE '%1%'
//        query.where(board.title.contains("1")); // where title like...

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

        booleanBuilder.or(board.title.contains("11")); // title like ...

        booleanBuilder.or(board.content.contains("11")); // content like ....

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));

        // paging
        this.getQuerydsl().applyPagination(pageable, query);

        //select * from board where title like '%1%'
        List<Board> list = query.fetch();

        // select Count(bno) from board where title like '%1%'
        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        // Querydsl -> JPQL 변환하기 위한 역할
        QBoard board = QBoard.board;

        // FROM board
        JPQLQuery<Board> query = from(board);

        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            /*
            * title LIKE '%1%'
            * OR content LIKE '%1%'
            * OR writer LIKE '%1%'
            * */

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for

            /*
            * WHERE (
            * title LIKE '%1%'
            * OR content LIKE '%1%'
            * OR writer LIKE '%1%'
            * )
            * */
            query.where(booleanBuilder);
        }//end if

        // AND bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        /*
        SELECT COUNT(bno)
         FROM board
         WHERE (
                title LIKE '%1%'
                OR content LIKE '%1%'
                OR writer LIKE '%1%'
            )
            AND bno > 0L
         ORDER BY bno DESC limit 1, 10;
        * */
        long count = query.fetchCount();

        /*
        * list: 실제 row 리스트들
        * pageable: 페이지 처리 필요정보
        * count: 전체 row 갯수
        *
        * 페이징 처리하려면 이것들이 모두 필요하므로 묶어서 넘기려고
           Page<E>를 상속받은 PageImpl<E> 클래스를 만들어 놓은 것이다.
        * */
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        // Querydsl -> JPQL로 변환
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        // 왼쪽 테이블인 board에 기준을 맞춰서 board는 다 출력해줘
        // board의 row(행)을 댓글이 존재하지 않아도 모두 출력해줘
        // From board LEFT OUTRE JOIN reply ON reply.board_bno = board.bno

        // From board;
        JPQLQuery<Board> query = from(board);

        // LEFT OUTRE JOIN reply ON reply.board_bno = board.bno
        query.leftJoin(reply).on(reply.board.eq(board));

        //GROUB BY board의 컬럼들
        query.groupBy(board);

        // OR 가 AND 보다 연산자 우선순위가 낮기 때문에 OR 조건들은 () 괄호로 묶어준다.
        if( (types != null && types.length > 0) && keyword != null ){

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for

            /*
            WHERE (
            board.title LIKE '%:keyword%'
            OR
            board.content LIKE '%:keyword%'
            OR
            board.writer LIKE '%:keyword%'
            )
            * */
            query.where(booleanBuilder);
        }

        // AND board.bno > 0
        query.where(board.bno.gt(0L));

        // SELECT bno, title, writer, regDate, COUNT(reply) replyCount
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")
        ));

        // paging 처리를 dtoQuery에 적용해줌(페이징 처리 sql문을 추가해줌)
        this.getQuerydsl().applyPagination(pageable,dtoQuery);

        // 쿼리 실행
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        // 현재 결과 전체 갯수
        long count = dtoQuery.fetchCount();

        // thymeleaf html에 전달하기 위해서 묶어서 리턴
        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        /* FROM board
           LEFT OUTER JOIN reply ON reply.bno=board.bno
        * */
        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // left join

        if( (types != null && types.length > 0) && keyword != null){

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } // end for
            boardJPQLQuery.where(booleanBuilder);

            /*
            WHERE (
            board.title LIKE '%:keyword%'
            OR
            board.content LIKE '%:keyword%'
            OR
            board.writer LIKE '%:keyword%'
            )
            * */
        }

        // GROUP BY board.bno, ...
        boardJPQLQuery.groupBy(board);

        // ORDER BY limit :skipRow, :getRows
        getQuerydsl().applyPagination(pageable, boardJPQLQuery); // paging

        // SELECT board.bno, ..., COUNT(reply)
        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        /*
        SELECT board.bno, ..., COUNT(reply)
         FROM board
         LEFT OUTER JOIN reply ON reply.bno=board.bno
         WHERE (
            board.title LIKE '%:keyword%'
            OR
            board.content LIKE '%:keyword%'
            OR
            board.writer LIKE '%:keyword%'
            )
         GROUP BY board.bno, ...
         ORDER BY limit :skipRow, :getRows;
        * */

        /*
        boardJPQLQuery.select(board, reply.countDistinct()) 처럼 결과를 2개(이상)을 요구하므로
        Tuple로 받으면 여러 개의 결과를 받을 수 있다.
        * */
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {


            Board board1 = (Board)tuple.get(board);              // Tuple의 1번째 요소
            long replyCount = tuple.get(1, Long.class);     // Tuple의 2번째 요소

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            // BoardImage를 BoardImageDTO 처리할 부분
            /*
           Board테이블과 BoardImage테이블은
           @OneToMany로 연결되어 있으므로 Board의 필드를 통해서 가져올 수 있다.

           SELECT *
            FROM boardImage
            WHERE boardImage.bno = :bno;
           * */
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());
            // BoardDTO에 BoardImage의 List까지 모두 저장함
            dto.setBoardImages(imageDTOS); // 처리된 BoardImageDTO들을 추가

            return dto;
        }).collect(Collectors.toList());

        // 현재 pagination이 적용된 Board의 조회한 row전에 갯수
        long totalCount = boardJPQLQuery.fetchCount();

        // Board(replyCount, BoardImageList), 페이징 처리 정보, 전체 갯수
        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
