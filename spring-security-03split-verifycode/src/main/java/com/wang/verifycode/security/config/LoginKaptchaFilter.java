package com.wang.verifycode.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.verifycode.security.exception.KaptchaNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author wangjubin
 */
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_KAPTCHA_KEY = "kaptcha";


    private String kaptchaParameter = SPRING_SECURITY_FORM_KAPTCHA_KEY;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                String kaptcha = userInfo.get(getKaptchaParameter());
                String sessionVerifyCode = String.valueOf(request.getSession().getAttribute("kaptcha"));
//                if (!ObjectUtils.isEmpty(kaptcha) && !ObjectUtils.isEmpty(sessionVerifyCode) &&
//                        kaptcha.equalsIgnoreCase(sessionVerifyCode)) {
                    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
                    this.setDetails(request, authRequest);
                    return this.getAuthenticationManager().authenticate(authRequest);
//                }
//                throw new KaptchaNotMatchException("VerifyCode error");


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return super.attemptAuthentication(request, response);
    }


}
