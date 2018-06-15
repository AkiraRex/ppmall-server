package com.ppmall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;

@Controller
@RequestMapping("/kill")
public class SeckillController {
	
	@RequestMapping(value = "/create_order.do")
	@ResponseBody
	public ServerResponse cerateOrder() {
		
		return null;
		
	}
	
	@RequestMapping(value = "/get_kill_url.do")
	@ResponseBody
	public ServerResponse getKillUrl() {
		
		return null;
		
	}
}
