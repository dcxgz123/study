package com.chen.blog.controller;

import com.chen.blog.service.CommentsService;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;
    @GetMapping("article/{id}")
    //comments/article/{id}
    public Result comments(@PathVariable("id") Long id){

        return commentsService.commentsByArticleId(id);
    }
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }

}
