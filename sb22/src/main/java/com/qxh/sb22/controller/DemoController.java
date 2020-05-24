package com.qxh.sb22.controller;

import com.example.weather.WeatherService;
import com.qxh.sb22.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private TestService testService;

//    @Autowired
//    private WeatherService weatherService;

    @RequestMapping("/test/{key}")
    public String test(@PathVariable String key) {
        return testService.test(key);
    }

//    @GetMapping("/weather")
//    public String weather() {
//        return weatherService.getType();
//    }

}
