package com.ppmall.dao;

import java.util.List;

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
}