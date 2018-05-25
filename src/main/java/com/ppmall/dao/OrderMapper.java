package com.ppmall.dao;

import com.ppmall.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    int updateByOrderNoSelective(Order order);

    List selectAll(Map paramMap);

    Order selectByOrderNo(Long orderNo);
}