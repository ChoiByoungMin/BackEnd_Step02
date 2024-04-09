package org.zerock.jdbcex.service;

import org.modelmapper.ModelMapper;
import org.zerock.jdbcex.dao.TodoDAO;
import org.zerock.jdbcex.domain.TodoVO;
import org.zerock.jdbcex.dto.TodoDTO;
import org.zerock.jdbcex.util.MapperUtil;

public enum TodoService {
    INSTANCE;

    private TodoDAO dao;
    private ModelMapper modelMapper;
    TodoService() {

        dao = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public void register(TodoDTO todoDTO) throws Exception {

        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);

        System.out.println("todoVO: " + todoVO);

        dao.insert(todoVO); //int를 반환하므로 이를 이용해서 예외처리도 가능
    }
}
