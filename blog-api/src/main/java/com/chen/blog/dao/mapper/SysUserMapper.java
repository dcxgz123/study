package com.chen.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.blog.dao.pojo.SysUser;
import com.chen.blog.vo.params.RegisterParam;
import org.springframework.stereotype.Repository;

@Repository

public interface SysUserMapper extends BaseMapper<SysUser> {
}
