package com.chen.blog.service.impl;

import com.chen.blog.dao.mapper.CategoryMapper;
import com.chen.blog.dao.pojo.Category;
import com.chen.blog.service.CategoryService;
import com.chen.blog.vo.CategoryVo;
import com.chen.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAllCategory() {
        List<Category> categories = categoryMapper.selectList(null);
        return Result.success(categories);
    }

    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(null);
        return Result.success(categories);
    }

    @Override
    public Result categoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(category);
    }
}
