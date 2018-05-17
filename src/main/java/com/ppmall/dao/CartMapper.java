package com.ppmall.dao;

import com.ppmall.pojo.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer i);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}