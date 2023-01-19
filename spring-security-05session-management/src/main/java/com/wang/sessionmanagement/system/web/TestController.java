package com.wang.sessionmanagement.system.web;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author wangjubin
 */
@RestController
public class TestController {

    @PostMapping("/test")
    public Object test() {
        MapBuilder<Object, Object> put = MapUtil.builder(new HashMap<>()).put("code", HttpStatus.HTTP_OK).put("message", "Test");
        return put.build();
    }
}
