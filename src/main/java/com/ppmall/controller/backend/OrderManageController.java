package com.ppmall.controller.backend;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getOrderList(int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Long orderNo) {
        return iOrderService.getOrderList(orderNo, pageNum, pageSize);
    }
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getOrderDetail(Long orderNo){
        return iOrderService.getOrderDetail(orderNo);
    }
}
