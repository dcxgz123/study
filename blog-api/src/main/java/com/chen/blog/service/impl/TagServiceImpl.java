package com.chen.blog.service.impl;

import com.chen.blog.dao.mapper.TagMapper;
import com.chen.blog.dao.pojo.Tag;
import com.chen.blog.service.TagService;
import com.chen.blog.vo.Result;
import com.chen.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        /**
         * 1.标签所拥有的文章数量最多 最热标签
         * 2.查询 根据tag_id 分组 计数，从大到小 排列 取前limit个
         */
        List<Long> tagIds =tagMapper.findHotsTagIds(limit);
        //需求的是tagId和tagName   Tag对象
        //select * from tag where id in(1,2,3,4)
        if (CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        List <Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);

    }

    @Override
    public Result all() {
        List<Tag> tags = tagMapper.selectList(null);
        return Result.success(tags);
    }

    @Override
    public Result allDetail() {
        List<Tag> tags = tagMapper.selectList(null);
        TagVo tagVo = new TagVo();
        List<TagVo> tagVoList = copyList(tags);
        return Result.success(tagVoList);
    }

    @Override
    public Result allDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(tag);
    }

    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVoList = new ArrayList<>();
        for(Tag tag : tags){
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
