package com.ppmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;
import com.ppmall.service.IRegionService;

@Controller
@RequestMapping("/region")
public class RegionController {
	@Autowired IRegionService iRegionService;
	
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse list(@RequestParam(value = "parentId", defaultValue = "1") int parentId) {
		return iRegionService.getRegionList(parentId);
	}
}
