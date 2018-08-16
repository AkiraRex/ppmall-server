package com.ppmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.ProductMapper;
import com.ppmall.pojo.Product;
import com.ppmall.service.IProductService;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.util.StringUtil;
import com.ppmall.vo.ProductVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "com.ppamll.service.ProductService")
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
    @Cacheable(key = "#root.caches[0].name + #productId", unless = "#result.getStatus() == 1")
	// key 缓存key 
	// #root.caches[0].name即类设置的缓存数据库(链表用于存储key的值)
	// #productId 为函数参数
	// unless 不存缓存的条件
	public ServerResponse getDetailById(int productId) {
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createErrorMessage("该商品已下架或被删除");
		}
		ProductVo productVo = new ProductVo(product);
		productVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
		
		return ServerResponse.createSuccess("获取成功", productVo);
	}

	@Override
	@CacheEvict(key = "#root.caches[0].name + #product.getId()")
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
	@CacheEvict(key = "#root.caches[0].name + #product.getId()")
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
