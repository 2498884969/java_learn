package com.qxh.sb22.controller;

import com.qxh.sb22.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private TestService testService;

    @RequestMapping("/test/{key}")
    public String test(@PathVariable String key) {
        return testService.test(key);
    }


}
