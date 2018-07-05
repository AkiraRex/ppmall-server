package com.ppmall.dao;

import java.util.List;

import com.ppmall.pojo.Advert;

public interface AdvertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Integer id);
    
    List<Advert> selectBySelective(Advert advert);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);
}