package com.ppmall.dao;

import java.util.List;

import com.ppmall.pojo.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    List selectByUserId(int userId);
    
    Shipping selcetDefault(Integer userId);
    
    int updateAllByUserIdSelective(Shipping record);
}