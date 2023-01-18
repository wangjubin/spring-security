package com.wang.springsecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wang.springsecurity.*.dao")
public class SpringSecuritySplitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecuritySplitApplication.class, args);
    }

}
