package com.chen.blog.vo.params;

import com.chen.blog.vo.CategoryVo;
import com.chen.blog.vo.TagVo;
import lombok.Data;

import java.util.List;


@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

    private String search;
}