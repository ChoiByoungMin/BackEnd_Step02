package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b01.dto.ReplyDTO;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController         // Rest방식/json 데이터로 응답
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")        // swagger-ui에 보여짐
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                     BindingResult bindingResult) throws BindException {

        /* @RequestBody ReplyDTO replyDTO
        * json 문자열로 전송되는 값들을 replyDTO에 저장하겠다.
        * */
        log.info(replyDTO);

        if (bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno",111L);

        return resultMap;
    }
}
