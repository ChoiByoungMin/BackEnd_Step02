package org.zerock.springex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.PageRequestDTO;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.service.TodoService;

import javax.validation.Valid;

@Controller
@RequestMapping("/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {

    private  final TodoService todoService;

//    // /todo/list
//    @RequestMapping("/list")
//    public void list(Model model){
//        log.info("todo list.......");
//
//        model.addAttribute("dtoList", todoService.getAll());
//    }

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

    @GetMapping({"/read", "/modify"})
    public void read(Long tno, PageRequestDTO pageRequestDTO, Model model){

        TodoDTO todoDTO = todoService.getOne(tno);
        log.info(todoDTO);

        model.addAttribute("dto", todoDTO);
    }

    @PostMapping("/remove")
    public String remove(Long tno, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){

        log.info("---------------remove---------------");
        log.info("tno: " + tno );

        todoService.remove(tno);

        return "redirect:/todo/list?" + pageRequestDTO.getLink();
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid TodoDTO todoDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            log.info("has errors..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("tno", todoDTO.getTno());
            return "redirect:/todo/modify";
        }

        log.info(todoDTO);
        todoService.modify(todoDTO);

        redirectAttributes.addAttribute("tno", todoDTO.getTno());
        return "redirect:/todo/read";
    }

    @GetMapping("list")
    public void list(@Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, Model model){

        log.info(pageRequestDTO);

        if(bindingResult.hasErrors()){
            // 디폴트 값을 가지게 된다.(page=1, size=10)
            // 첫번째 페이지가 나오도록
            pageRequestDTO = PageRequestDTO.builder().build();
        }


        model.addAttribute("responseDTO", todoService.getList(pageRequestDTO));
    }

}
