package com.chen.blog.controller;

import com.chen.blog.dao.pojo.SysUser;
import com.chen.blog.utils.UserThreadLocal;
import com.chen.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
