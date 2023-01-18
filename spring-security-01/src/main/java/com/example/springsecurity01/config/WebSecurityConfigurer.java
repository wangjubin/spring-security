package com.example.springsecurity01.config;

import com.example.springsecurity01.handler.MyAuthenticationFailedHandler;
import com.example.springsecurity01.handler.MyAuthenticationSuccessHandler;
import com.example.springsecurity01.handler.MyLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * @author wangjubin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer {

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//

    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager inMemoryUserDetailsManager
                = new InMemoryUserDetailsManager();
        UserDetails u1 = User.withUsername("zhangs")
                .password("{noop}111").roles("USER").build();
        inMemoryUserDetailsManager.createUser(u1);
        return inMemoryUserDetailsManager;
    }


    @Bean
    public DefaultSecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http
                //开启表单认证
                .formLogin()
                .loginProcessingUrl("/doLogin")
                //登录成功处理器
                .successHandler(new MyAuthenticationSuccessHandler())
                //登录失败处理器
                .failureHandler(new MyAuthenticationFailedHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                //退出时是否清除session
                .invalidateHttpSession(true)
                //退出时是否清除认证信息
                .clearAuthentication(true)
                //退出成功处理器
                .logoutSuccessHandler(new MyLogoutSuccessHandler())

                .and().csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .anyRequest().authenticated())


        ;

        return http.build();
    }

    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("111");
        System.out.println(encode);
    }

}
