package com.ppmall.dao;

import java.util.List;

import com.ppmall.pojo.Region;

public interface RegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Region record);

    int insertSelective(Region record);

    Region selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Region record);

    int updateByPrimaryKey(Region record);
    
    List<Region> selectListByParentId(Integer parentId);
}