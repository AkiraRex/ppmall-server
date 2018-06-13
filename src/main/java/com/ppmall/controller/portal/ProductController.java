package com.ppmall.controller.portal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.IProductService;
import com.ppmall.util.StringUtil;
import com.ppmall.vo.ProductVo;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private IProductService iProductService;

	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getProductList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageNum", defaultValue = "10") int pageSize, Integer categoryId, String keyword,
			String orderBy, HttpSession session) {

		Map paramMap = new HashMap<>();
		paramMap.put("categoryId", categoryId);
		paramMap.put("keyword", keyword);

		if (StringUtil.isNotBlank(orderBy) && !orderBy.equals("default"))
			paramMap.put("orderBy", orderBy.replace("_", " "));

		return iProductService.getProductListPortal(pageNum, pageSize, paramMap);
	}

	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> getDetail(HttpSession session, int productId) {
		ServerResponse response = iProductService.getDetailById(productId);
		ProductVo product = (ProductVo) response.getData();
		if (product != null)
			return response;
		return ServerResponse.createErrorMessage("该商品已下架或被删除");

	}

}
