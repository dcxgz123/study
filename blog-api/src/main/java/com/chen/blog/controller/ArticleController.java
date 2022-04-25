package com.chen.blog.controller;

import com.chen.blog.common.aop.LogAnnotation;
import com.chen.blog.common.cache.Cache;
import com.chen.blog.service.ArticleService;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.params.ArticleParam;
import com.chen.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据脚护
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //加上此注解 代表要对此接口记录日志
    @Cache(expire=5*60*1000,name="ArticleList")
    @LogAnnotation(module="文章",operator="获取文章列表")
    public Result listArticle(@RequestBody PageParams pageParams){
        Result result = articleService.listArticle(pageParams);
        return result;
    }
    @Cache(expire=5*60*1000,name="hot_article")
    @PostMapping("/hot")
    public Result hotArticleList (){
        int limit = 3 ;
        Result result = articleService.hotArticleList(limit);
        return result;
    }
    @Cache(expire=5*60*1000,name="hot_article")
    @PostMapping("/new")
    public Result newArticleList (){
        int limit = 3 ;
        Result result = articleService.newArticleList(limit);
        return result;
    }
    @PostMapping("/listArchives")
    public Result listArchives(){
        Result result = articleService.listArchives();
        return result;
    }
    @PostMapping("view/{id}")
    public Result findArticlesByid(@PathVariable("id") Long articleId){
        Result result = articleService.findArticlesById(articleId);
        return result;
    }
    @PostMapping("publish")
    public Result publishArticle(@RequestBody ArticleParam articleParam){
        Result result;
        if (articleParam.getId()!=null){
            result = articleService.UpdateArticleById(articleParam);
        }else {
            result =articleService.publishArticle(articleParam);}
        return result;
    }
    @PostMapping("/{id}")
    public Result findArticlesFormById(@PathVariable("id") Long articleId){
        Result result = articleService.findArticlesFormById(articleId);
        return result;
    }
}
