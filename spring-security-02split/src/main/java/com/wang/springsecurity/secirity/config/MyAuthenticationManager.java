//package com.wang.springsecurity.secirity.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
///**
// * @author wangjubin
// */
//@Component
//public class MyAuthenticationManager implements AuthenticationManager {
//
//    private final UserDetailsService userDetailsService;
//
//
//    @Autowired
//    public MyAuthenticationManager(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//
//    }
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        final UserDetails userDetail = userDetailsService.loadUserByUsername(authentication.getName());
//        PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();
//        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
//            throw new BadCredentialsException("Wrong password");
//        }
//        return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(), userDetail.getAuthorities());
//
//    }
//}
