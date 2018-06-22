package com.ppmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.ServerResponse;

@Controller
@RequestMapping("/manage/activity" )
public class ActivityManageController {
	@RequestMapping(value = "/save.do", method = RequestMethod.POST)
	public ServerResponse saveActivity() {
		return null;
	}
	
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, int productId) {
        return null;
    }
	
	@RequestMapping(value = "/set_status.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse setStatus(HttpSession session, int productId) {
        return null;
    }
}
