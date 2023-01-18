package com.example.springsecurity01.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjubin
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello security";
    }
}
