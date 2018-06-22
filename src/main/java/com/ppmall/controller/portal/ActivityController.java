package com.ppmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.rabbitmq.message.ActivityMessage;
import com.ppmall.redis.RedisUtil;
import com.ppmall.service.ActivityService;

@Controller
@RequestMapping("/activity")
public class ActivityController {
	@Autowired
	private ActivityService iSeckillService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@RequestMapping(value = "/create_order.do")
	@ResponseBody
	public ServerResponse createOrder(int activityId,int shippingId,HttpSession session) {
		
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		
		redisUtil.get("");
		
		ActivityMessage message = new ActivityMessage();
		message.setActivityId(activityId);
		message.setProductId(26);
		message.setQuantity(1);
		message.setShippingId(shippingId);
		message.setUserId(22);
		return iSeckillService.createOrder(message);
		
	}
	
	@RequestMapping(value = "/get_kill_url.do")
	@ResponseBody
	public ServerResponse getKillUrl() {
		
		return null;
		
	}
	
	@RequestMapping(value = "/get_kill_list.do")
	@ResponseBody
	public ServerResponse getKillList() {
		
		return null;
		
	}
}
