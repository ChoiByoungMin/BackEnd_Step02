package org.zerock.w2.filter;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


@WebFilter(urlPatterns = {"/todo/*"})
@Log4j2
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("Login check filter......");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        // 세션에 로그인 정보가 있으니 로그인이라고 판단하고 서블릿 전송
        if (session.getAttribute("loginInfo") != null) {

            chain.doFilter(request, response);

            return;
        }

        // session에 loginInfo 값이 없다면 쿠키를체크
        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        // 세션에도 없고 쿠키도 없다면 그냥 로그인으로
        if (cookie == null) {
            resp.sendRedirect("/login");
            return;
        }

        // 쿠키가 존재하는 상황이라면
        log.info("cookie 는 존재하는 상황");
        // uuid값
        String uuid = cookie.getValue();

        try{
            //데이터 베이스 확인, 브라우저의 remember-me 쿠키의 값과 maria-db의 uuid값이 일치하는지
            MemberDTO memberDTO = MemberService.INSTANCE.getByUUID(uuid);

            log.info("쿠키의 값으로 조회한 사용자 정보: " + memberDTO);
            
            // remember-me의 정보가 maria-db의 uuid와 일치하지 않는다.
            if(memberDTO == null){
                throw new Exception("Cookie value is not valid");
            }
            //회원 정보를 세션에 저장 서블릿 전송
            session.setAttribute("loginInfo", memberDTO);
            chain.doFilter(request, response);
        } catch (Exception e){
            e.printStackTrace();
            resp.sendRedirect("/login");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String name) {

        if(cookies == null || cookies.length == 0){
            return null;
        }

        // cookie객체를 얻는데, 추가로 optional에서 제공하는 메서드를 사용할 수 있다.
        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(ck -> ck.getName().equals(name))
                .findFirst();

        // result가 정상이면, 존재하면 cookie객체를 리턴하고, 아니면 널값을 리턴한다.
        return result.isPresent() ? result.get() : null;
    }
}
