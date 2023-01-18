package com.example.springsecurity01.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjubin
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> map = MapUtil.builder(new HashMap<String, Object>())
                .put("msg", "注销成功")
                .put("status", 500)
                .build();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSONUtil.toJsonStr(map));
    }
}
