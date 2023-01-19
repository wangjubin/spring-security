package com.wang.sessionmanagement.security.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.wang.sessionmanagement.security.handler.MyAuthenticationEntryPoint;
import com.wang.sessionmanagement.security.handler.MyAuthenticationFailureHandler;
import com.wang.sessionmanagement.security.handler.MyAuthenticationSuccessHandler;
import com.wang.sessionmanagement.security.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjubin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer {

    private final FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    public WebSecurityConfigurer(FindByIndexNameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> list = ListUtil.list(false,
                User.builder().username("root1").password("{noop}123").roles("admin").build(),
                User.builder().username("root2").password("{noop}123").roles("admin").build(),
                User.builder().username("root3").password("{noop}123").roles("admin").build()
        );

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(list);
        return inMemoryUserDetailsManager;
    }

    @Bean
    public DefaultSecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/doLogin")
                .successHandler(new MyAuthenticationSuccessHandler())
                .failureHandler(new MyAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .and().exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .and().userDetailsService(userDetailsService())
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/doLogin")
                .and()
                //开启会话管理
                .sessionManagement()
                //允许同一个用户只允许创建一个会话
                .maximumSessions(1)
                //会话过期处理
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    Map<Object, Object> result = MapUtil.builder(new HashMap<>()).put("code", HttpStatus.HTTP_INTERNAL_ERROR)
                            .put("msg", "当前回话以失败，请重新登录").build();
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSONUtil.toJsonStr(result));
                })
                //控制使用的SessionRegistry实现
                .sessionRegistry(sessionRegistry())
                //登录之后禁止再次登录
                .maxSessionsPreventsLogin(true);


        return http.build();
    }
    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry(){
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }
}
