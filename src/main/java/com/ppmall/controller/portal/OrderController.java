package com.ppmall.controller.portal;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.Configs;
import com.ppmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
	public ServerResponse pay(Long orderNo,HttpSession session)  {
		String path = session.getServletContext().getRealPath("upload");
		ServerResponse response = null;
		try {
			response = iOrderService.payForOrder(orderNo,path);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			response = ServerResponse.createErrorMessage("支付宝错误");
		} catch (IOException e) {
			e.printStackTrace();
			response = ServerResponse.createErrorMessage("错误");
		}
		return response;
	}
	
	@RequestMapping(value = "/alipay_callback.do", method = RequestMethod.POST)
	@ResponseBody
	public Object alipayCallback(HttpSession session,HttpServletRequest request)  {
		Map paramMap = new HashMap<>();
		Enumeration paramNames = request.getParameterNames();
		// 遍历获取参数
		while (paramNames.hasMoreElements()) {
			String name = (String) paramNames.nextElement();
			paramMap.put(name, request.getParameter(name));
		}
		
		// 遍历完毕
		// 支付宝验签名
		paramMap.remove("sign_type");

		try {
			boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(paramMap, Configs.getAlipayPublicKey(),"utf-8","RSA2");
			System.out.println("---------------------" + alipayRSACheckedV2 + "------------------------------");
			if (!alipayRSACheckedV2)
				return ServerResponse.createErrorMessage("非法请求");
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServerResponse response = iOrderService.alipayCallback(paramMap);

		if(response.isSuccess())
			return Const.AliPayReponse.SUCCESS;
		else
			return Const.AliPayReponse.ERROR;
	}
}
