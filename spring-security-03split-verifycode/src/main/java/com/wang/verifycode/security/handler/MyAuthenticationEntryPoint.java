package com.wang.verifycode.security.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjubin
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Map<String, Object> build = MapUtil.builder(new HashMap<String, Object>())
                .put("msg", "请先登录"+authException.getMessage()+"--"+request.getRequestURI())
                .put("code", HttpStatus.HTTP_MOVED_PERM).build();

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSONUtil.toJsonStr(build));
    }
}
