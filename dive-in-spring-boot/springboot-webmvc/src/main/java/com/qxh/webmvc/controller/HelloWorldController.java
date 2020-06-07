package com.qxh.webmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

@Controller
public class HelloWorldController {

    @RequestMapping("/")
    public String index( Model model) {

        return "index";
    }

}
