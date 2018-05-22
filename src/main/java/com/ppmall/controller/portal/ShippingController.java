package com.ppmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.IShippingService;

@Controller
@RequestMapping("/shipping")
public class ShippingController {
	
	@Autowired
	private IShippingService iShippingService;
	
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getShippingList(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iShippingService.getShippingList(user.getId());
	}

}
