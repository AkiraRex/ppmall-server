package com.ppmall.service;

import com.ppmall.common.ServerResponse;


public interface IOrderService {
    ServerResponse getOrderList(Long orderNum, int pageNum, int pageSize);

    ServerResponse getOrderDetail(Long orderNo);
}
