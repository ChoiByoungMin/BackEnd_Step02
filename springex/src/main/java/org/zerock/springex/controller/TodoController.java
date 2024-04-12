package org.zerock.springex.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.springex.dto.TodoDTO;

@Controller
@RequestMapping("/todo")
@Log4j2
public class TodoController {

    // /todo/list
    @RequestMapping("/list")
    public void list(){
        log.info("todo list.......");
    }

    // /todo/register
    //@RequestMapping(value = "register", method = RequestMethod.GET)
    @GetMapping("/register")

    public void registerGET(){
        log.info("GET todo register.....");
    }


    @PostMapping("/register")

    public void registerPOST(TodoDTO todoDTO){
        log.info("POST todo register.....");
        log.info(todoDTO);
    }

}
