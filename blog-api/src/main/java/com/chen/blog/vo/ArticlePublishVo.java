package com.chen.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ArticlePublishVo {
    @JsonSerialize(using = ToStringSerializer.class)

    private Long articleId;
}
