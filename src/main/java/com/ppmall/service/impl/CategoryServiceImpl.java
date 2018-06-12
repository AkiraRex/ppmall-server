package com.ppmall.service.impl;

import com.google.common.collect.Sets;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CategoryMapper;
import com.ppmall.pojo.Category;
import com.ppmall.redis.RedisUtil;
import com.ppmall.service.ICategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public ServerResponse addCategory(int parentId, String CategoryName) {
		Category category = new Category();

		category.setName(CategoryName);
		category.setParentId(parentId);
		category.setStatus(true);

		int insertCount = categoryMapper.insert(category);

		if (insertCount > 0){
			redisUtil.del(Const.Cache.ALL_CATEGORY);
			getAllCategoryList();
			return ServerResponse.createSuccessMessage("添加品类成功");
		}

		return ServerResponse.createErrorMessage("添加品类失败");
	}

	@Override
	public ServerResponse<List> getCategory(int parentId) {
		List categoryList = categoryMapper.selectCategoryByParentId(parentId);
		// findChildCategory(categorySet,categoryId;

		if (categoryList.size() == 0) {
			logger.info("未找到相关子类");
		}

		return ServerResponse.createSuccess(categoryList);

	}

	@Override
	public ServerResponse getCategoryAndChildren(int categoryId) {
		Set<Category> categorySet = Sets.newHashSet();
		findChildCategory(categorySet, categoryId);
		return ServerResponse.createSuccess(categorySet);
	}

	@Override
	public ServerResponse getCategoryParent(int categoryId) {
		Set<Category> categorySet = Sets.newHashSet();
		findParentCategory(categorySet, categoryId);
		// Set<String> returnSet = Sets.newHashSet();

		Set<Category> treeSet = new TreeSet(categorySet);

		String categoryParent = "/";

		for (Category category : treeSet) {
			categoryParent = categoryParent + category.getName() + "/";
		}

		return ServerResponse.createSuccess(categoryParent);
	}

	@Override
	public ServerResponse setCategoryName(int categoryId, String categoryName) {
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);

		int updateCount = categoryMapper.updateByPrimaryKeySelective(category);

		if (updateCount > 0) {
			redisUtil.del(Const.Cache.ALL_CATEGORY);
			getAllCategoryList();
			return ServerResponse.createSuccessMessage("修改成功");
		}

		return ServerResponse.createErrorMessage("修改失败");
	}

	// 递归算法,算出子节点
	private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}
		// 查找子节点,递归算法一定要有一个退出的条件

		List<Category> categoryList = categoryMapper.selectCategoryAndChildByParentId(categoryId);
		for (Category categoryItem : categoryList) {
			findChildCategory(categorySet, categoryItem.getId());
		}
		return categorySet;
	}

	// 递归算法,算出父节点
	private Set<Category> findParentCategory(Set<Category> categorySet, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}
		// 查找子节点,递归算法一定要有一个退出的条件
		List<Category> categoryList = categoryMapper.selectCategoryAndParent(categoryId);
		for (Category categoryItem : categoryList) {
			findParentCategory(categorySet, categoryItem.getParentId());
		}
		return categorySet;
	}

	@Override
	public ServerResponse getAllCategoryList() {
		// TODO Auto-generated method stub
		List allList = (List) redisUtil.get(Const.Cache.ALL_CATEGORY);

		if (allList == null || allList.size() == 0) {
			allList = new ArrayList<>();
			List<Category> lel1List = categoryMapper.selectCategoryAndChildByParentId(0);// 查询出1级品类
			for (Category category : lel1List) {
				List<Category> lel2List = categoryMapper.selectCategoryAndChildByParentId(category.getId());
				Map map = new HashMap<>();
				map.put("subCategoryList", lel2List);
				map.put("id", category.getId());
				map.put("name", category.getName());
				map.put("parentId", category.getParentId());
				allList.add(map);
			}
			redisUtil.set("allCategory", allList);
		}

		return ServerResponse.createSuccess("获取成功", allList);
	}

}
