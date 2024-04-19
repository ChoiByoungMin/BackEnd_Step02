package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;

import java.util.List;

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
}
