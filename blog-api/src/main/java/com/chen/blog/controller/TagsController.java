package com.chen.blog.controller;

import com.chen.blog.service.TagService;
import com.chen.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagsController {
    @Autowired
    private TagService tagService;
    @GetMapping
    public Result all(){
        return tagService.all();
    }
    @GetMapping("/hot")
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }
    @GetMapping("detail")
    public Result allDetail(){
        return tagService.allDetail();
    }
    @GetMapping("detail/{id}")
    public Result allDetailById(@PathVariable("id") Long id){
        return tagService.allDetailById(id);
    };
}
