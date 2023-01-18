package com.wang.verifycode.security.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjubin
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> build = MapUtil.builder(new HashMap<String, Object>())
                .put("msg", "退出成功")
                .put("code", HttpStatus.HTTP_OK).build();

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(JSONUtil.toJsonStr(build));
    }
}
