package com.ppmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.IIndexService;

@Controller
@RequestMapping("/index")
public class IndexController {
	@Autowired IIndexService iIndexService;

	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse list() {
		return iIndexService.getIndexData();
	}
}
