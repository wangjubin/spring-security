package com.wang.rememberme.security.config;

import cn.hutool.core.lang.UUID;
import com.wang.rememberme.security.handler.MyAuthenticationEntryPoint;
import com.wang.rememberme.security.handler.MyAuthenticationFailureHandler;
import com.wang.rememberme.security.handler.MyAuthenticationSuccessHandler;
import com.wang.rememberme.security.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

/**
 * @author wangjubin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http. csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeHttpRequests(author -> {
                    author.anyRequest().authenticated();
                })
                .addFilterAt(loginFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint()).and()
                .userDetailsService(userDetailsService())
                .formLogin()
                .and()
                //开启记住我功能  cookie 进行实现  1.认证成功保存记住我 cookie 到客户端   2.只有 cookie 写入客户端成功才能实现自动登录功能
                .rememberMe()
                //设置自动登录使用哪个 rememberMeServices
                .rememberMeServices(rememberMeServices())
                .and().logout().logoutSuccessHandler(new MyLogoutSuccessHandler());
        http.securityContext((securityContext) -> securityContext.requireExplicitSave(false));
        return http.build();
    }


    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter loginKaptchaFilter = new LoginFilter();
        loginKaptchaFilter.setFilterProcessesUrl("/doLogin");
        loginKaptchaFilter.setAuthenticationManager(authenticationManager);
        loginKaptchaFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        loginKaptchaFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        loginKaptchaFilter.setRememberMeServices(rememberMeServices());
        return loginKaptchaFilter;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailsService(), persistentTokenRepository());
    }
}
