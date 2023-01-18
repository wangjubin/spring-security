package com.wang.verifycode.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @description 验证码异常类
 * @author wangjubin
 */
public class KaptchaNotMatchException extends AuthenticationException {
    public KaptchaNotMatchException(String detail) {
        super(detail);
    }

    public KaptchaNotMatchException(String detail, Throwable ex) {
        super(detail, ex);
    }
}
