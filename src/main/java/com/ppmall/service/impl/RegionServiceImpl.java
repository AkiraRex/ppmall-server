package com.ppmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.dao.RegionMapper;
import com.ppmall.pojo.Region;
import com.ppmall.service.IRegionService;

@Service("iRegionService")
public class RegionServiceImpl implements IRegionService {

	@Autowired
	RegionMapper regionMapper;
	
	@Override
	public ServerResponse getRegionList(int parentId) {
		// TODO Auto-generated method stub
		List<Region> regionList = regionMapper.selectListByParentId(parentId);
		return ServerResponse.createSuccess("获取成功", regionList);
	}

}
