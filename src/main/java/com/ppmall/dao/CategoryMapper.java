package com.ppmall.dao;

import java.util.List;

import com.ppmall.pojo.Category;

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
	
	List selectCategoryByIds(Integer ids[]);
}