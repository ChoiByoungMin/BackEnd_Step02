package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Board;

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
public interface BoardRepository  extends JpaRepository<Board, Long> {
}
