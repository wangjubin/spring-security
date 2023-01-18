package com.wang.verifycode.system.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjubin
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserDetailsService userDetailsService;

    @GetMapping("getCurrentUser")
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("getUser/{username}")
    public Object getUser(@PathVariable(value = "username") String username) {
        return userDetailsService.loadUserByUsername(username);
    }

}
