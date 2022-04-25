package com.chen.blog.service;

import com.chen.blog.dao.dos.Archives;
import com.chen.blog.vo.ArticleBodyVo;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.params.ArticleParam;
import com.chen.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticleList(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticleList(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticlesById(Long articleId);


    Result publishArticle(ArticleParam articleParam);


    Result findArticlesFormById(Long articleId);

    Result UpdateArticleById(ArticleParam articleParam);
}
