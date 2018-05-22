package com.ppmall.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CartMapper;
import com.ppmall.pojo.Cart;
import com.ppmall.service.ICartService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.vo.CartProductVo;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public ServerResponse getCartCount(int userId) {
		// TODO Auto-generated method stub
		int cartCount = cartMapper.selectCountByUserId(userId);
		return ServerResponse.createSuccess("获取成功", cartCount);
	}

	@Override
	public ServerResponse getCartList(int userId) {
		// TODO Auto-generated method stub
		List<CartProductVo> cartList = cartMapper.selectCartProductListByUserId(userId);
		double cartTotalPrice = 0;
		boolean allChecked = true;
		for (CartProductVo vo : cartList) {
			double totalPrice = vo.getProductTotalPrice();
			cartTotalPrice += totalPrice;
			if (vo.getProductChecked() == 0)
				allChecked = false;
		}

		Map returnMap = new HashMap<>();
		returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));
		returnMap.put("allChecked", allChecked);
		returnMap.put("cartTotalPrice", cartTotalPrice);
		returnMap.put("cartProductVoList", cartList);

		return ServerResponse.createSuccess("获取成功", returnMap);
	}
	
	@Override
	public ServerResponse getCartListByChecked(int userId, int checked) {
		// TODO Auto-generated method stub
		List<CartProductVo> cartList = cartMapper.selectCartProductListByUserIdAndChecked(userId, checked);
		double cartTotalPrice = 0;
		boolean allChecked = true;
		for (CartProductVo vo : cartList) {
			double totalPrice = vo.getProductTotalPrice();
			cartTotalPrice += totalPrice;
			if (vo.getProductChecked() == 0)
				allChecked = false;
		}

		Map returnMap = new HashMap<>();
		returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));
		returnMap.put("allChecked", allChecked);
		returnMap.put("cartTotalPrice", cartTotalPrice);
		returnMap.put("cartProductVoList", cartList);

		return ServerResponse.createSuccess("获取成功", returnMap);
	}
	
	@Override
	public ServerResponse addToCart(int productId, int count, int userId) {
		// TODO Auto-generated method stub
		Cart cartDb = cartMapper.selectCartByProductId(userId, productId);

		if (cartDb != null) {
			cartDb.setQuantity(cartDb.getQuantity() + count);
			cartDb.setChecked(1);
			cartMapper.updateByPrimaryKey(cartDb);
		} else {
			Date date = DateUtil.getDate();

			Cart cart = new Cart();
			cart.setChecked(1);
			cart.setCreateTime(date);
			cart.setProductId(productId);
			cart.setQuantity(count);
			cart.setUpdateTime(date);
			cart.setUserId(userId);

			cartMapper.insert(cart);
		}
		return getCartList(userId);
	}

	@Override
	public ServerResponse deleteCart(int[] productIds, int userId) {
		// TODO Auto-generated method stub
		Map paramMap = new HashMap<>();
		paramMap.put("productIds", productIds);
		paramMap.put("userId", userId);
		cartMapper.deleteByProductIds(paramMap);
		return getCartList(userId);
	}

	@Override
	public ServerResponse updateCart(Integer productId, int userId, Integer count,int checked) {
		// TODO Auto-generated method stub
		cartMapper.updateCartByProductId(userId, productId, count, checked);
		return getCartList(userId);
	}
}
