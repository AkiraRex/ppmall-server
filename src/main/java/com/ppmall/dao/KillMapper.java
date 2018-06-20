package com.ppmall.dao;

import java.util.List;
import java.util.Map;

import com.ppmall.pojo.Kill;

public interface KillMapper {
   
	int deleteByPrimaryKey(Integer id);

    int insert(Kill record);

    int insertSelective(Kill record);

    Kill selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Kill record);

    int updateByPrimaryKey(Kill record);
    
    List<Kill> selectByActivityDuration(Map paramMap); 
}