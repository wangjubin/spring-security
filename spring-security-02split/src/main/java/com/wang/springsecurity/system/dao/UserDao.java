package com.wang.springsecurity.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.springsecurity.system.bean.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangjubin
 */
@Mapper
public interface UserDao  extends BaseMapper<User> {

}
