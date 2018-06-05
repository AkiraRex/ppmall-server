package com.ppmall.controller.portal;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alipay.api.AlipayApiException;
import com.ppmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	IOrderService iOrderService;

	@RequestMapping(value = "/get_order_cart_product.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getOrderCartList(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		int userId = currentUser.getId();
		return iOrderService.getOrderCart(userId);
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse createOrder(int shippingId, HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		try {
			return iOrderService.createOrder(currentUser.getId(), shippingId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ServerResponse.createErrorMessage("创建失败");
		}
	}

	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getOrderList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		return iOrderService.getOrderList(currentUser.getId(), pageNum, pageSize);
	}

	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getOrderDetail(Long orderNo, HttpSession session) {
		return iOrderService.getOrderDetail(orderNo);
	}
	
	@RequestMapping(value = "/cancel.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse cancelOrder(Long orderNo) {
		return iOrderService.cancelOrder(orderNo);
	}
	
	@RequestMapping(value = "/pay.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse pay(Long orderNo,HttpServletResponse response, Writer writer) throws AlipayApiException {
		return iOrderService.payForOrder(orderNo);
	}
}
