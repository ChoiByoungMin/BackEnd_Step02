package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet (name = "todoRegisterController", value = "/todo/register")
@Log4j2
public class TodoRegisterController extends HttpServlet {

    private TodoService todoService = TodoService.INSTANCE;
    private final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("/todo/register GET ......");

        /* 로그인 정보 유무에  따른 register.jsp  이동 여부 결정 --- start */
        HttpSession session = req.getSession();

        if(session.isNew()){
            log.info("이전에 접속한 정보가 없는 사용자 - JSESSIONID 쿠키가 새로 만들어진 사용자");
            resp.sendRedirect("/login");
            return;
        }

        // 접속한 적은 있지만, 로그인을 하지 않았다면
        if(session.getAttribute("loginInfo") == null){
            log.info("이전에 접속한 정보가 존재하는 사용자 - 로그인한 정보가 없는 사용자");
            resp.sendRedirect("/login");
            return;
        }

        /* 로그인 정보 유무에 따른 register.jsp 이동 여부 결정 --- end */

        // 정상적인 경우라면 입려 화면으로
        req.getRequestDispatcher("/WEB-INF/todo/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TodoDTO todoDTO = TodoDTO.builder()
                .title(req.getParameter("title"))
                .dueDate(LocalDate.parse(req.getParameter("dueDate"), DATEFORMATTER))
                .build();

        log.info("/todo/register POST ......");
        log.info(todoDTO);
        try {
            todoService.register(todoDTO);
        }catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("/todo/list");
    }
}
