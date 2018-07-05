package com.ppmall.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.AdvertMapper;
import com.ppmall.dao.CategoryMapper;
import com.ppmall.dao.ProductMapper;
import com.ppmall.pojo.Advert;
import com.ppmall.util.PropertiesUtil;

@Service("iIndexService")
public class IIndexServiceImpl implements IIndexService {

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private AdvertMapper advertMapper;

	@Override
	public ServerResponse getIndexData() {
		// TODO Auto-generated method stub
		List hotProductList = productMapper.selectHotProduct(3);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("beginTime", new Date(new Date().getTime() - Const.ExpiredType.ONE_DAY * 7));
		paramMap.put("endTime", new Date());
		List newProductList = productMapper.selectAll(paramMap);

		List categoryList = categoryMapper.selectCategoryByParentId(0);

		Advert advert = new Advert();
		advert.setPosition(Const.AdvertPosition.INDEX_BANNER);
		advert.setStatus(Const.ValidStatus.VALID);
		List bannerList = advertMapper.selectBySelective(advert);

		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("hotProductList", hotProductList);
		returnMap.put("newProductList", newProductList);
		returnMap.put("categoryList", categoryList);
		returnMap.put("bannerList", bannerList);
		returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));
		return ServerResponse.createSuccess(returnMap);
	}

}
