package com.chen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.blog.dao.dos.Archives;
import com.chen.blog.dao.dos.ArticleTag;
import com.chen.blog.dao.mapper.*;
import com.chen.blog.dao.pojo.*;
import com.chen.blog.service.*;
import com.chen.blog.utils.UserThreadLocal;
import com.chen.blog.vo.*;
import com.chen.blog.vo.params.ArticleParam;
import com.chen.blog.vo.params.PageParams;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return  Result.success(copyList(records,true,true));

    }

//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1.   分页查询 article 数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if(pageParams.getCategoryId()!=null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId()!=null){
//            LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> tags = articleTagMapper.selectList(lambdaQueryWrapper);
//            List<Article> articles = new ArrayList<>();
//            for (ArticleTag articleTag: tags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if(articleIdList.size()>0){
//                //and id in (1,2,3)            }
//                queryWrapper.in(Article::getId,articleIdList);
//        }
//        }
//        if(pageParams.getYear()!=null){
//            Archives archives1 = new Archives();
//            archives1.setYear(Integer.valueOf(pageParams.getYear()));
//            archives1.setMonth(Integer.valueOf(pageParams.getMonth()));
//            List<Article> archives = articleMapper.findArchives(archives1);
//            return Result.success(archives);
//        }
//        //是否置顶进行排序
//        //order by create_date desc
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        System.out.println(articleVoList+"dddddddddddddddddddddd");
//        return Result.success(articleVoList);
//    }


    @Override
    public Result hotArticleList(int limit) {
        /**
         *  查询最热文章
         */
        List<Article> articles = articleMapper.findHotArticleList(limit);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticleList(int limit) {
        /**
         * 查询最新文章
         */
        List<Article> articles = articleMapper.findNewArticleList(limit);
        return Result.success(copyList(articles,false,false));
    }

    /**
     * 文章归档
     * @return
     */
    @Override
    public Result listArchives() {
        List<Archives> archivesList=articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;
    @Override
    public Result findArticlesById(Long articleId) {
        /**
         * 1.根据id查询文章信息
         * 2.根据bodyId和categoryId 去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo =copy(article,true,true,true,true);
        //查看完文章了，新增阅读数，有没有问题？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响查看文章的操作
        //线程池 可以把更新操作 扔到线程池中去执行， 和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }
    @Transactional
    @Override
    public Result publishArticle(ArticleParam articleParam) {
        /**
         * 1.发布文章 目的 构建Article对象
         * 2.作者id
         * 3.标签 要将标签加入到关联列表中
         */
        Article article = new Article();
        BeanUtils.copyProperties(articleParam,article);
        article.setAuthorId(UserThreadLocal.get().getId());
        article.setWeight(0);
        article.setCategoryId(articleParam.getCategory().getId());
        articleMapper.insert(article);
        //tag
        List<TagVo> tags =articleParam.getTags();
        if(tags != null){
            for (TagVo tag :tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        BeanUtils.copyProperties(articleParam.getBody(),articleBody);
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        article.setCreateDate(System.currentTimeMillis());
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticlePublishVo articlePublishVo = new ArticlePublishVo();
        articlePublishVo.setArticleId(article.getId());
        return Result.success(articlePublishVo);
    }

    @Override
    public Result findArticlesFormById(Long articleId) {
        /**
         * 根据文章Id查出markdown数据
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo =copy(article,true,false,true,true);
        return Result.success(articleVo);
    }
    @Override
    @Transactional
    public Result UpdateArticleById(ArticleParam articleParam) {
        /**
         * 根据文章Id修改文章
         */
        Article article = new Article();
        article.setCategoryId(articleParam.getCategory().getId());
        BeanUtils.copyProperties(articleParam,article);
        UpdateWrapper<Article> updateWrapper =new UpdateWrapper<>();
        updateWrapper.eq("id",article.getId());
        articleMapper.update(article,updateWrapper);
        UpdateWrapper<ArticleBody> bodyUpdateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",article.getBodyId());
        ArticleBody articleBody = new ArticleBody();
        BeanUtils.copyProperties(articleParam.getBody(),articleBody);
        articleBodyMapper.update(articleBody,bodyUpdateWrapper);
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagMapper.delete(lambdaQueryWrapper);
        List<TagVo> tags =articleParam.getTags();
        for (TagVo tag :tags) {
            Long articleId = article.getId();
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tag.getId());
            articleTag.setArticleId(articleId);
            articleTagMapper.insert(articleTag);
        }
        ArticlePublishVo articlePublishVo = new ArticlePublishVo();
        articlePublishVo.setArticleId(article.getId());
       return Result.success(articlePublishVo);
    }


    private List<ArticleVo> copyList(List<Article> records,boolean isTag ,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record: records){
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private ArticleVo copy (Article article,boolean isTag ,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserVoById(authorId));
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
