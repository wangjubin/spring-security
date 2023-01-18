package com.wang.rememberme.security.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author wangjubin
 * @description :重写UsernamePasswordAuthenticationFilter，将表单提交改为json
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = userInfo.get(getUsernameParameter());
                username = username != null ? username.trim() : "";
                String password = userInfo.get(getPasswordParameter());
                password = password != null ? password : "";
                String remember = userInfo.get(AbstractRememberMeServices.DEFAULT_PARAMETER);
                if(!ObjectUtils.isEmpty(remember)){
                    //将remember-me放到attribute中，自定义【MyPersistentTokenBasedRememberMeServices】的从attribute中取
                    request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER,remember);
                }
                UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                logger.error("Error reading");
            }

        }

        return super.attemptAuthentication(request, response);


    }

}
