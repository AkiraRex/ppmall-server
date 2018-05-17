package com.ppmall.service;

import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.Product;

import java.util.Map;

public interface IProductService {
    ServerResponse getProductList(int pageNum, int pageSize, Map paramMap);

    ServerResponse getDetailById(int productId);

    ServerResponse saveProduct(Product product);

    ServerResponse setStatus(Product product);

}
