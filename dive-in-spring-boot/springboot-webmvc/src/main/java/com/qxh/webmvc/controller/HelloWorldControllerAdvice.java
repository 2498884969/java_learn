package com.qxh.webmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

@ControllerAdvice(assignableTypes = HelloWorldController.class)
public class HelloWorldControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> onException( Throwable throwable) {
        return ResponseEntity.ok(throwable.getMessage());
    }

    @ModelAttribute("message")
    public String message() {
        return "hello world";
    }

    @ModelAttribute("acceptLanguage")
    public String acceptLanguage(@RequestHeader("Accept-Language") String acceptLanguage) {
        return acceptLanguage;
    }

//    @ModelAttribute("sessionId")
//    public String sessionId(@CookieValue("JSESSIONID") Cookie cookie){
//        return cookie.getValue();
//    }


}
