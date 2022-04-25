package com.chen.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.blog.dao.dos.Archives;
import com.chen.blog.dao.pojo.Article;
import com.chen.blog.vo.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    List<Article> findHotArticleList(int limit);

    List<Article> findNewArticleList(int limit);

    List<Archives> listArchives();

    List<Article> findArchives(Archives archives);

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month
                               );

//  List<Article> findArticleById(int id);
}
