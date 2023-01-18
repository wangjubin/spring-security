package com.wang.verifycode.security.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 登录失败处理器
 * @author wangjubin
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Object> build = MapUtil.builder(new HashMap<String, Object>())
                .put("msg", "登录失败"+exception.getMessage())
                .put("code", HttpStatus.HTTP_INTERNAL_ERROR).build();

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSONUtil.toJsonStr(build));
    }
}
