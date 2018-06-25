package com.ppmall.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CategoryMapper;
import com.ppmall.pojo.Category;
import com.ppmall.redis.RedisUtil;
import com.ppmall.service.ICategoryService;
import com.ppmall.util.PropertiesUtil;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public ServerResponse addCategory(int parentId, String CategoryName, String mainImage) {
		Category category = new Category();

		category.setName(CategoryName);
		category.setParentId(parentId);
		category.setStatus(true);

		int insertCount = categoryMapper.insert(category);

		if (insertCount > 0) {
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

	@Override
	public ServerResponse getAllCategoryList() {
		// TODO Auto-generated method stub
		List allList = (List) redisUtil.get(Const.Cache.ALL_CATEGORY);
		Map returnMap = new HashMap<>();
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
		returnMap.put("categoryList", allList);
		returnMap.put("imgHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));

		return ServerResponse.createSuccess("获取成功", returnMap);
	}

	@Override
	@Transactional
	public ServerResponse delCategory(int categoryId) {
		// TODO Auto-generated method stub
		Set<Category> categorySet = new HashSet<>();
		findChildCategory(categorySet, categoryId);

		for (Category category : categorySet) {
			categoryMapper.deleteByPrimaryKey(category.getId());
		}
		redisUtil.del(Const.Cache.ALL_CATEGORY);
		getAllCategoryList();
		return ServerResponse.createSuccessMessage("删除成功");
	}

	@Override
	public ServerResponse setCategoryImage(int categoryId, String mainImage) {
		// TODO Auto-generated method stub
		Category category = new Category();
		category.setId(categoryId);
		category.setMainImage(mainImage);

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
		// 查找父节点,递归算法一定要有一个退出的条件
		List<Category> categoryList = categoryMapper.selectCategoryAndParent(categoryId);
		for (Category categoryItem : categoryList) {
			findParentCategory(categorySet, categoryItem.getParentId());
		}
		return categorySet;
	}

	@Override
	public ServerResponse getCategoryDetail(int categoryId) {
		// TODO Auto-generated method stub
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		return ServerResponse.createSuccess(category);
	}
}
