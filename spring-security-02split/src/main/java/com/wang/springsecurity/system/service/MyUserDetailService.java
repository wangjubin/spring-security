package com.wang.springsecurity.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wang.springsecurity.system.bean.User;
import com.wang.springsecurity.system.dao.RoleDao;
import com.wang.springsecurity.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author wangjubin
 */
@Service
public class MyUserDetailService implements UserDetailsService {
    private final UserDao userDao;

    private final RoleDao roleDao;

    @Autowired
    public MyUserDetailService(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userDao.selectOne(lambdaQueryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        user.setRoles(roleDao.getRolesByUid(user.getId()));
        return user;


    }
}
