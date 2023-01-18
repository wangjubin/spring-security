package com.wang.verifycode.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.verifycode.system.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangjubin
 */
@Mapper
public interface UserDao  extends BaseMapper<User> {

}
