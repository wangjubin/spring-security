package com.wang.springsecurity.system.dao;

import com.wang.springsecurity.system.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wangjubin
 */
@Mapper
public interface RoleDao {

    List<Role> getRolesByUid(Integer uid);



}
