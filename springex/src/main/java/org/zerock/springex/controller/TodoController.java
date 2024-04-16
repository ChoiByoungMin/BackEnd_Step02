package org.zerock.springex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.service.TodoService;

import javax.validation.Valid;

@Controller
@RequestMapping("/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {

    private  final TodoService todoService;

    // /todo/list
    @RequestMapping("/list")
    public void list(Model model){
        log.info("todo list.......");

        model.addAttribute("dtoList", todoService.getAll());
    }

    // /todo/register
    //@RequestMapping(value = "register", method = RequestMethod.GET)
    @GetMapping("/register")
    public void registerGET(){
        log.info("GET todo register.....");
    }


    @PostMapping("/register")
    public String registerPOST(@Valid TodoDTO todoDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        log.info("POST todo register.....");

        /* todoDTO의 제약조건이 오류가 발생했을 때
        * */
        if(bindingResult.hasErrors()){
            log.info("has errors..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/todo/register";
        }

        log.info(todoDTO);

        todoService.register(todoDTO);

        return "redirect:/todo/list";
    }

}
