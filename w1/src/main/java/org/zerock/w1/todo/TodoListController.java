package org.zerock.w1.todo;
import org.zerock.w1.todo.dto.TodoDTO;
import org.zerock.w1.todo.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="todoListController", urlPatterns = "/todo/list")
public class TodoListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("/todo/list");

        List<TodoDTO> dtoList = TodoService.INSTANCE.getList();
        req.setAttribute("list", dtoList);

        //System.out.println("DB로부터 목록을 꺼내어 list.jsp에 전달");

        /* 이 코드를 아래처럼 축약했다.
        RequestDispatcher rd = req.getRequestDispatcher("todoList.jsp");
        rd.forward(req, resp);
        */
        req.getRequestDispatcher("/WEB-INF/todo/list.jsp").forward(req, resp);
    }
}
