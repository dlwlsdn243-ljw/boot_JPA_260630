package com.example.boot.security;

import com.example.boot.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailuerHandler implements AuthenticationFailureHandler {

    // redirect 데이터 정보를 가지고 있는 객체(redirect로 경로저장 (이동) 역할)
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패시
        // error 메시지 띄우기
        String errorMessage; // error메시지 저장용 변수

        // BadCredentialsException : 페스워드 틀렸을 경우
        if (exception instanceof BadCredentialsException){
            errorMessage = "email / password가 일치하지 않습니다.";
        } else {
            errorMessage = "관리자에게 문의해주세요. admin@gmail.com";
        }
        log.info(">>> errormessage >> {}", errorMessage);

        // 로그인 페이지로 해당 애러 메시지르 리다이렉트
        request.getSession().setAttribute("errMsg", errorMessage);

        redirectStrategy.sendRedirect(request, response, "/user/login");

    }
}