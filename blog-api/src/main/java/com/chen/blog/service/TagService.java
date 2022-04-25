package com.chen.blog.service;

import com.chen.blog.vo.Result;
import com.chen.blog.vo.TagVo;
import org.springframework.stereotype.Service;

import java.util.List;
public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result all();

    Result allDetail();

    Result allDetailById(Long id);
}
