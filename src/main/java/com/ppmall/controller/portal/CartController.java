package com.ppmall.controller.portal;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.ICartService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private ICartService iCartService;

	@RequestMapping(value = "/get_cart_product_count.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<Integer> getCartCount(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		int userId = currentUser.getId();
		return iCartService.getCartCount(userId);
	}

	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<List> getCartList(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		int userId = currentUser.getId();
		return iCartService.getCartList(userId);
	}

	@RequestMapping(value = "/add.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse add(int productId, int count, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.addToCart(productId, count, user.getId());
	}

	@RequestMapping(value = "/delete_product.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse delete(String productIds, HttpSession session) {
		String productId[] = productIds.split(",");
		int productIdsI[] = new int[productId.length];
		for (int i = 0; i < productId.length; i++) {
			productIdsI[i] = Integer.valueOf(productId[i]);
		}
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.deleteCart(productIdsI, user.getId());
	}

	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse update(int productId, int count, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(productId, user.getId(), count, 1);
	}

	@RequestMapping(value = "/select.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse select(int productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(productId, user.getId(), null, 1);
	}

	@RequestMapping(value = "/un_select.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse unSelect(int productId, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(productId, user.getId(), null, 0);
	}
	
	@RequestMapping(value = "/select_all.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse selectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(null, user.getId(), null, 1);
	}
	
	@RequestMapping(value = "/un_select_all.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse unSelectAll(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		return iCartService.updateCart(null, user.getId(), null, 0);
	}
}
