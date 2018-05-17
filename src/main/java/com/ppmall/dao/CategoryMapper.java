package com.ppmall.dao;

import com.ppmall.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List selectCategoryByParentId(int parentId);

    List selectCategoryAndChildByParentId(int parentId);

    List selectCategoryAndParent(int id);
}