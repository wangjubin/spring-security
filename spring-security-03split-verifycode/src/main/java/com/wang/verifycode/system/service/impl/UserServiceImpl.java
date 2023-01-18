package com.wang.verifycode.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.verifycode.system.dao.RoleDao;
import com.wang.verifycode.system.dao.UserDao;
import com.wang.verifycode.system.entity.User;
import com.wang.verifycode.system.service.MyIService;
import com.wang.verifycode.system.service.UserPasswordService;
import com.wang.verifycode.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author wangjubin
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService, UserPasswordService, MyIService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userDao.selectOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户名不存在!");
        }
        user.setRoles(roleDao.getRolesByUid(user.getId()));
        return user;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<User>();
        lambdaUpdateWrapper.set(User::getPassword, newPassword);
        lambdaUpdateWrapper.eq(User::getUsername, user.getUsername());
        boolean update = this.update(lambdaUpdateWrapper);
        if (update) {
            ((User) user).setPassword(newPassword);
        }
        return user;
    }
}
