package com.chen.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.blog.dao.mapper.SysUserMapper;
import com.chen.blog.dao.pojo.SysUser;
import com.chen.blog.utils.JWTUtils;
import com.chen.blog.utils.QiniuUtils;
import com.chen.blog.utils.UserThreadLocal;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.UploadAvatarVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        //原始文件名称  比如 aa.png
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString()+"." + StringUtils.substringAfterLast(originalFilename,".");
        //上传文件到七牛云
        //降低自身应用服务器的带宽消耗
        boolean upload = qiniuUtils.upload(file,fileName);
        if(upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @PostMapping("avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString()+"." + StringUtils.substringAfterLast(originalFilename,".");
        //上传文件到七牛云
        //降低自身应用服务器的带宽消耗
        boolean upload = qiniuUtils.upload(file,fileName);
        if(upload){
            Long id = UserThreadLocal.get().getId();
            LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(SysUser::getId,id);
            SysUser sysUser = new SysUser();
            sysUser.setAvatar(QiniuUtils.url + fileName);
            sysUserMapper.update(sysUser,lambdaQueryWrapper);
            String token = JWTUtils.createToken(id);
            SysUser sysUser1 = sysUserMapper.selectById(id);
            redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser1),1, TimeUnit.DAYS);
            UploadAvatarVo uploadAvatarVo = new UploadAvatarVo();
            uploadAvatarVo.setToken(token);
            uploadAvatarVo.setUrl(QiniuUtils.url + fileName);
            return Result.success(uploadAvatarVo);
        }
        return Result.fail(20001,"上传失败");
    }
}
