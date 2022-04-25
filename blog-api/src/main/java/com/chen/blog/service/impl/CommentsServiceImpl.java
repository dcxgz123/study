package com.chen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chen.blog.dao.mapper.CommentMapper;
import com.chen.blog.dao.pojo.Article;
import com.chen.blog.dao.pojo.Comment;
import com.chen.blog.dao.pojo.SysUser;
import com.chen.blog.service.ArticleService;
import com.chen.blog.service.CommentsService;
import com.chen.blog.service.SysUserService;
import com.chen.blog.service.ThreadService;
import com.chen.blog.utils.UserThreadLocal;
import com.chen.blog.vo.ArticlePublishVo;
import com.chen.blog.vo.CommentVo;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.UserVo;
import com.chen.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章id 查询 评论列表 从comment 表中查询
         * 2.根据作者的id 查询作者的信息
         * 3.判断 如果 level = 1 要去查询它有没有子评论
         * 4.如果有 根据评论id 进行查询
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    @Autowired
    private ThreadService threadService;
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        ArticlePublishVo articlePublishVo = new ArticlePublishVo();
        articlePublishVo.setArticleId(commentParam.getArticleId());
        threadService.updateArticleCommentCount(commentParam.getArticleId());
        return Result.success(articlePublishVo);
    }


    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for(Comment comment:comments){
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //作者信息
        Long authorId = comment.getAuthorId();

        UserVo userVoById = this.sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVoById);
        //子评论
        Integer level = comment.getLevel();
        if (1==level){
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);

        }
        //to Users 给谁评论
        if(level>1){
            Long toUid = comment.getToUid();
            UserVo toUserVo =this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);

        return copyList(commentMapper.selectList(queryWrapper));
    }
}
