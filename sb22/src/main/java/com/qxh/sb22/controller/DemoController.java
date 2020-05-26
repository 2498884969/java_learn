package com.qxh.sb22.controller;

import com.qxh.sb22.service.TestService;
import com.qxh.starter.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private TestService testService;

    @Resource(name = "demo")
    private DemoService demoService;


    @RequestMapping("/test/{key}")
    public String test(@PathVariable String key) {
        return testService.test(key);
    }

    @GetMapping("/demo")
    public String sayWhat(){
        return demoService.say();
    }

}
