package com.chen.blog.service;

import com.chen.blog.dao.pojo.Category;
import com.chen.blog.vo.CategoryVo;
import com.chen.blog.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAllCategory();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
