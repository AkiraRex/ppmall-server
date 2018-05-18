package com.ppmall.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.ServerResponse;
import com.ppmall.dao.CartMapper;
import com.ppmall.service.ICartService;
import com.ppmall.vo.CartProductVo;

@Service("iCartService")
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public ServerResponse getCartCount(int userId) {
		// TODO Auto-generated method stub
		int cartCount = cartMapper.selectCountByUserId(userId);
		return ServerResponse.createSuccess("获取成功",cartCount);
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
			if(vo.getProductChecked()==0)
				allChecked = false;
		}
		
		Map returnMap = new HashMap<>();
		returnMap.put("allChecked", allChecked);
		returnMap.put("cartTotalPrice", cartTotalPrice);
		returnMap.put("cartProductVoList", cartList);
		
		return ServerResponse.createSuccess("获取成功",returnMap);
	}

}
