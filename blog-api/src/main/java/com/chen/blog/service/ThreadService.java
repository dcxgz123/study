package com.chen.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chen.blog.dao.mapper.ArticleMapper;
import com.chen.blog.dao.mapper.CommentMapper;
import com.chen.blog.dao.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async("taskExecutor")
public class ThreadService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
        //期望此操作在线程池执行 不会影响原有的主线程
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个 为了在多线程环境下，线程安全
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        articleMapper.update(articleUpdate,updateWrapper);

    }
    public void updateArticleCommentCount(Long articleId){

        Long count = commentMapper.findCountComments(articleId);
        System.out.println("dddddddd"+count);
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",articleId).set("comment_counts",count);
        articleMapper.update(null,updateWrapper);
    }



}
