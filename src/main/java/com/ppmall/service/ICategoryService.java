package com.ppmall.service;

import com.ppmall.common.ServerResponse;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
	ServerResponse addCategory(int parentId, String CategoryName, String mainImage);

	ServerResponse<List> getCategory(int parentId);

	ServerResponse getCategoryAndChildren(int categoryId);

	ServerResponse setCategoryName(int categoryId, String categoryName);

	ServerResponse getCategoryParent(int categoryId);

	ServerResponse getAllCategoryList();
	
	ServerResponse delCategory(int categoryId);
	
	ServerResponse setCategoryImage(int categoryId, String mainImage);
}
