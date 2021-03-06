package com.chen.blog.controller;

import com.chen.blog.service.RegisterService;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.params.LoginParam;
import com.chen.blog.vo.params.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @PostMapping
    public Result register(@RequestBody RegisterParam registerParam){
        Result result=registerService.UserRegister(registerParam);
        return result;
    }


}
