package com.ppmall.controller.portal;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/**
	 * 小程序接口(获取品类)
	 * @param session
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategory(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategory(parentId);
    }
	
	@RequestMapping(value = "/get_category_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategoryDetail(HttpSession session, @RequestParam(value = "id", defaultValue = "0") int id) {
        return iCategoryService.getCategoryDetail(id);
    }
}
