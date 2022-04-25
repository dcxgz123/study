package com.chen.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

}
