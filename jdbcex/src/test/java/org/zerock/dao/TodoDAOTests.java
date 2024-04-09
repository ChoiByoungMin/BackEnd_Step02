package org.zerock.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.jdbcex.dao.TodoDAO;
import org.zerock.jdbcex.domain.TodoVO;

import java.time.LocalDate;
import java.util.List;

public class TodoDAOTests {
    private TodoDAO todoDAO;

    /*
    * @Test를 하기전에 미리 호출되어 객체를
    * 준비하는 용도로 사용하는 애노테이션
    * */
    @BeforeEach
    public void ready() {
        todoDAO = new TodoDAO();
    }

    @Test
    public void testTime() throws Exception {

        System.out.println(todoDAO.getTime());
    }

    @Test
    public void testTime2() throws Exception {
        System.out.println(todoDAO.getTime2());
    }

    @Test
    public void testInsert() throws Exception {
        TodoVO todoVO = TodoVO.builder()
                .title("Sample Title...")
                .dueDate(LocalDate.of(2021,12,31))
                .build();

        todoDAO.insert(todoVO);
    }

    @Test
    public void testList() throws Exception {
        List<TodoVO> list = todoDAO.selectAll();
        list.forEach(vo -> System.out.println(vo));
    }

    @Test
    public void testSelectOne() throws Exception {

        Long tno = 1L;
        TodoVO vo = todoDAO.selectOne(tno);

        System.out.println(vo);
    }

    @Test
    public void testUpdateOne() throws Exception {
        TodoVO todoVO = TodoVO.builder()
                .tno(1L)
                .title("Sample Title...")
                .dueDate(LocalDate.of(2021,12,31))
                .finished(true)
                .build();
        todoDAO.updateOne(todoVO);
    }
}
