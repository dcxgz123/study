package com.chen.blog.dao.dos;

import com.chen.blog.vo.TagVo;
import lombok.Data;

import java.util.List;
@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}
