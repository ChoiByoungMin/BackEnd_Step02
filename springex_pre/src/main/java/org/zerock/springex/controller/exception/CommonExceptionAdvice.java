package org.zerock.springex.controller.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Array;
import java.util.Arrays;

@ControllerAdvice
@Log4j2
public class CommonExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public String exceptNumber(Exception exception) {

        log.error("----------------------------------------");
        log.error(exception.getMessage());

        StringBuffer buffer = new StringBuffer("<ul>");

        buffer.append("<li>" + exception.getMessage() + "</li>");

        Arrays.stream(exception.getStackTrace()).forEach(stackTraceElement -> {
            buffer.append("<li>" + stackTraceElement.toString() + "</li>");
        });
        buffer.append("</ul>");

        return buffer.toString();
    }

    /*
    * 404에러는 담당 Controller에 주소 대응 메서드가 없을 경우 발생한다.
    * */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound() {

        /* 아래는 redirect: 를 주지 않았다.
        * 그러므로 /WEB-INF/views/custom404.jsp로 이동한다.
        * 결국 브라우저는 custom404.jsp화면을 보게된다.
        * */
        return "custom404";
    }

}
