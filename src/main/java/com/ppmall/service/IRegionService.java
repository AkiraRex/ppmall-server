package com.ppmall.service;

import com.ppmall.common.ServerResponse;

public interface IRegionService {
	
	ServerResponse getRegionList(int parentId);
	
}
