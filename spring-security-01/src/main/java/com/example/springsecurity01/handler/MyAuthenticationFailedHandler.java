package com.example.springsecurity01.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjubin
 */
public class MyAuthenticationFailedHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String, Object> map = MapUtil.builder(new HashMap<String, Object>())
                .put("msg", "登录失败: "+exception.getMessage())
                .put("status", 500)
                .build();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSONUtil.toJsonStr(map));
    }
}
