package com.wang.verifycode.security.config;

import com.wang.verifycode.security.handler.MyAuthenticationEntryPoint;
import com.wang.verifycode.security.handler.MyAuthenticationFailureHandler;
import com.wang.verifycode.security.handler.MyAuthenticationSuccessHandler;
import com.wang.verifycode.security.handler.MyLogoutSuccessHandler;
import com.wang.verifycode.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * @author wangjubin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer {

    private final UserService userService;

    @Autowired
    public WebSecurityConfigurer(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("continue");
        http.csrf().disable()
                .authorizeHttpRequests(author -> {
                    author.requestMatchers("/kaptcha/vc").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterAt(loginKaptchaFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userService)
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint()).and()
                .formLogin()
                .and().rememberMe()
                .and().logout().logoutSuccessHandler(new MyLogoutSuccessHandler());
        http.securityContext((securityContext) -> securityContext.requireExplicitSave(false));
        return http.build();
    }


    @Bean
    public LoginKaptchaFilter loginKaptchaFilter(AuthenticationManager authenticationManager) {
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        loginKaptchaFilter.setFilterProcessesUrl("/doLogin");
        loginKaptchaFilter.setAuthenticationManager(authenticationManager);
        loginKaptchaFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        loginKaptchaFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return loginKaptchaFilter;
    }
}
