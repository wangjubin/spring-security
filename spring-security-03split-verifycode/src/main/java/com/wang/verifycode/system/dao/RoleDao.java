package com.wang.verifycode.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.verifycode.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wangjubin
 */
@Mapper
public interface RoleDao  extends BaseMapper<Role> {

    List<Role> getRolesByUid(Integer uid);



}
