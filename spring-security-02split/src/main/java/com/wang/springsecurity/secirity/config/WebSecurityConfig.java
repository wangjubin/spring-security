package com.wang.springsecurity.secirity.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjubin
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .and().logout().logoutSuccessHandler((request, response, authentication) -> {
                    Map<String, Object> result = MapUtil.builder(new HashMap<String, Object>())
                            .put("msg", "注销成功")
                            .put("code", HttpStatus.HTTP_OK).build();
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(JSONUtil.toJsonStr(result));
                })
                .and()
                .authorizeRequests(author -> {
                    author
                            .anyRequest().authenticated();
                })
                .addFilterAt(loginFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)

                .exceptionHandling()
                .authenticationEntryPoint((req, resp, ex) -> {
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
                    resp.getWriter().println("请认证之后再去处理!" + ex.getMessage());
                })
                .and().csrf().disable();
        ;
        return http.build();
    }


    @Bean
    public Filter loginFilter(AuthenticationManager authenticationManager) {

        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler((req, resp, authentication) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录成功");
            result.put("用户信息", authentication.getPrincipal());
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(HttpStatus.HTTP_OK);
            resp.getWriter().println(JSONUtil.toJsonStr(result));
        });
        //认证失败处理
        loginFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录失败: " + ex.getMessage());
            resp.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().println(JSONUtil.toJsonStr(result));
        });
        return loginFilter;

    }


}
