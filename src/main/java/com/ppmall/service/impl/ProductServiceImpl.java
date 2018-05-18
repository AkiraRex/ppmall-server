package com.ppmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.ProductMapper;
import com.ppmall.pojo.Product;
import com.ppmall.service.IProductService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductMapper productMapper;

	@Override
	public ServerResponse getProductList(int pageNum, int pageSize, Map paramMap) {
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectAll(paramMap);
		PageInfo<Product> pageResult = new PageInfo<>(productList);
		return ServerResponse.createSuccess("获取成功", pageResult);
	}

	@Override
	public ServerResponse getDetailById(int productId) {
		Product product = productMapper.selectByPrimaryKey(productId);
		//String subImgs[] = product.getSubImages().split(",");
		//product.setSubImages(subImgs.toString());

		Map returnMap = new HashMap();
		returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));
		returnMap.put("id",product.getId());
		returnMap.put("categoryId",product.getCategoryId());
		returnMap.put("name",product.getName());
		returnMap.put("subtitle",product.getSubtitle());
		returnMap.put("mainImage",product.getMainImage());
		returnMap.put("subImages",product.getSubImages());
		returnMap.put("detail",product.getDetail());
		returnMap.put("price",product.getPrice());
		returnMap.put("stock",product.getStock());
		returnMap.put("createTime", DateUtil.getDateString(product.getCreateTime()));
		returnMap.put("updateTime",DateUtil.getDateString(product.getUpdateTime()));

		return ServerResponse.createSuccess("获取成功", returnMap);
	}

	@Override
	public ServerResponse saveProduct(Product product) {
		Integer productId = product.getId();

		if (StringUtil.isNotBlank(product.getSubImages())) {
			String subImg = product.getSubImages();
			product.setMainImage(subImg.split(",")[0]);
		}
		if (productId == null) {
			productMapper.insert(product);
			return ServerResponse.createSuccessMessage("添加成功");
		} else {
			productMapper.updateByPrimaryKey(product);
			return ServerResponse.createSuccessMessage("修改成功");
		}
	}

	@Override
	public ServerResponse setStatus(Product product) {
		productMapper.updateByPrimaryKeySelective(product);
		return ServerResponse.createSuccessMessage("修改成功");

	}

	@Override
	public ServerResponse getProductListPortal(int pageNum, int pageSize, Map paramMap) {
		// TODO Auto-generated method stub
		ServerResponse response = null;
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<Product> productList = productMapper.selectAll(paramMap);
			PageInfo<Product> pageResult = new PageInfo<>(productList);

			Map returnMap = new HashMap();
			returnMap.put("pageInfo",pageResult);
			returnMap.put("host", PropertiesUtil.getProperty("ftp.server.http.prefix"));

			response = ServerResponse.createSuccess("获取成功", returnMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = ServerResponse.createErrorStatus(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		return response;
	}
}
