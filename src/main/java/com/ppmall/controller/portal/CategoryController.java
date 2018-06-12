package com.ppmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.ICategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private ICategoryService iCategoryService;

	@RequestMapping(value = "/get_all_category.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getAllCategoryList(HttpSession session) {
		return iCategoryService.getAllCategoryList();
	}
}
