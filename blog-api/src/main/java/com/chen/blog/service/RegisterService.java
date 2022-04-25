package com.chen.blog.service;

import com.chen.blog.vo.Result;
import com.chen.blog.vo.params.RegisterParam;

public interface RegisterService {
    /**
     * 注册用户
     */
    Result UserRegister(RegisterParam registerParam);


}
