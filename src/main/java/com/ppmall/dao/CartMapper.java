package com.ppmall.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ppmall.pojo.Cart;

public interface CartMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Cart record);

	int insertSelective(Cart record);

	Cart selectByPrimaryKey(Integer i);

	int updateByPrimaryKeySelective(Cart record);

	int updateByPrimaryKey(Cart record);

	int selectCountByUserId(int userId);

	List selectCartListByUserId(int userId);

	List selectCartProductListByUserId(int userId);
	
	List selectCartProductListByUserIdAndChecked(@Param("userId")int userId,@Param("checked")int checked);

	int deleteByProductIds(Map paramMap);

	Cart selectCartByProductId(@Param("userId") int userId, @Param("productId") int productId);
	
	int updateCartByProductId(@Param("userId") int userId, @Param("productId") Integer productId,@Param("quantity") Integer count,@Param("checked") int checked);
	
}