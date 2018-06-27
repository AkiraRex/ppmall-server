package com.ppmall.dao;

import com.ppmall.pojo.Auth;

public interface AuthMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(Auth record);

	int insertSelective(Auth record);

	Auth selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Auth record);

	int updateByPrimaryKey(Auth record);

	Auth selectByOpenId(String openId);

}