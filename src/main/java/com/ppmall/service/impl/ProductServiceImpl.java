package com.ppmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.ProductMapper;
import com.ppmall.pojo.Product;
import com.ppmall.service.IProductService;
import com.ppmall.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return ServerResponse.createSuccess("获取成功", product);
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
}
