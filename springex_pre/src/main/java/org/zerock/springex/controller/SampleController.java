package org.zerock.springex.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.TodoDTO;

import java.time.LocalDate;

// (page)Controller역할을 하는 bean으로 생성 부탁해
@Controller
@Log4j2
public class SampleController {

    // /hello에 et요청이 들어오면 이 메서드가 처리한다.
    @GetMapping("/hello")
    public void hello() {
        log.info("hello");

        // 리턴이 없으면 자동으로 /WEB-INF/views/hello.jsp로 이동하게된다
    }

//    요청이 전달될 때 name변수와 age변수의 값이 전달되므로 저장하라

    @GetMapping("/ex1")
    public void ex1(String name, int age){
        log.info("ex1.........");
        log.info("name:" +name);
        log.info("age:" +age);
    }

    /* @RequestParam
    * 1) 웹에서 전달되는 parameter와 수신하는 메서드 파라미터의이름이 일치하지 않아도 받을 수 있다.
    * 2) 만약 전달되는 값이 없을 때 기본값을 지정할 수 있다.
    * */
    @GetMapping("/ex2")
    public void ex2(@RequestParam(name = "name", defaultValue = "AAA") String name,
                    @RequestParam(name = "age", defaultValue = "20")int age){
        log.info("ex2........");
        log.info("name: " + name);
        log.info("age: " + age);

        /* 이 처리가 끝나고 /WEB-INF/views/ex2.jsp로 이동할 때
        * String name과 int age는 request의 공간에 자동으로 저장 공유된다.
        * */
    }


    /* 날짜를 사용할 때 많이 사용하는 클래스
    * java.util.Date
    * java.sql.Date
    * java.time.LocalDate
    *
    * 웹에서 날짜정보는 String으로 전달된다.
    * 이 때 포맷 변환이 필요ㅏ다.
    *
    * 그래서 메서드 parameter를 String dueDate로 한 다음에
    * java 변환 코드로 처리하는 방법이 있지만.
    * 스프링은 이것을 보다 편하도록 해준다.ㄹ
    * */
    @GetMapping("/ex3")
    public void ex3(LocalDate dueDate){
        log.info("ex3........");
        log.info("dueDate: " + dueDate);
    }

    @GetMapping("/ex4")
    public void ex4(Model model){

        log.info("----------------------");

        
        model.addAttribute("message", "Hello World");
    }

    /* 웹에서 수신된 parameter들은 todoDTO의 동일한 필드에 저장되고
    * todoDTO라는 이름으로 request에 전달된다
    * name이라는 이름으로 Albert가 request에 전달된다.
    * 
    * request에 전달된다는 의미는 결국 jsp에서 꺼내어 쓸수 있다는 의미
    * */
    @GetMapping("/ex4_1")
    public void ex4Extra(@ModelAttribute("dto") TodoDTO todoDTO, Model model){

        log.info(todoDTO);

    }

    @GetMapping("/ex5")
    public String ex5(RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute("name","ABC");
        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/ex6";
    }

    @GetMapping("/ex6")
    public void ex6() {

    }

    @GetMapping("/ex7")
    public void ex7(String p1, int p2){
        log.info("p1........."+p1);
        log.info("p2........."+p2);
    }
}
