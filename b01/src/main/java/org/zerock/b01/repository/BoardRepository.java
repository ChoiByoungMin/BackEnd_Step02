package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import java.util.Optional;

/* 인터페이스를 만들고
JpaRepository를 상속받는데,
JpaRepository<사용 (테이블) 클래스명, pk의 자료형>

이렇게만 하면 자동으로Jpa라이브러리에 의해서
자식 객체가 생성되어 Spring Container에 포함된다.

Board에 대한 입출력이 필요하면
@Autowired
private BoardRepository boardRepository;

나 생성자 주입을 통해서 사용하면 된다.
*  */
public interface BoardRepository  extends JpaRepository<Board, Long>, BoardSearch {

    // 1) 쿼리메서드
    // http://spring.io/projects/spring-data-jpa

//    Page<Board> findTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);

    // JPQL -  JPA에서 정한 표준 sql, jpa에서 사용하는 Hibernate에서 NativeQuery로 자동 변환해서
    // DBMS에 전달한다.
//    @Query("select b from Board b where b.title like  concat('%',:keyword,'%')")
//    Page<Board> findKeyword(String keyword, Pageable pageable);

    // MariaDB에 적합한 sql - NativeQuery
//    @Query(value = "select now()", nativeQuery = true)
//    String getTime();

    /*
    * Board가 LAZY일 때
    * 연결된 자식 필드인 imageSet을 @EntityGraph로 선언하면
    * 아래 JPQL을 실행할 때 자식 테이블과의 필드 연결을 통해서 함께 조회하게 된다.
    * */
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno =:bno")
    Optional<Board> findByIdWithImages(Long bno);
}

    // 4) QueryDsl - 프로그래밍(객체 QueryDsl - 프로그래밍(객체, 메서드 호출) -> JPQL -> NativQuery)
