package com.ppmall.controller.backend;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.ICategoryService;
import com.ppmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping(value = "/add_category.do")
    @ResponseBody
    public ServerResponse<String> updateInformation(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.addCategory(parentId, categoryName);
    }

    @RequestMapping(value = "/get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategory(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategory(parentId);
    }

    @RequestMapping(value = "/get_category_child.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategoryAndChildren(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategoryAndChildren(parentId);
    }

    @RequestMapping(value = "/get_category_parent.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> getCategoryParent(HttpSession session, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.getCategoryParent(parentId);
    }

    @RequestMapping(value = "/set_category_name.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List> setCategoryName(HttpSession session, int categoryId, String categoryName) {
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }


}
