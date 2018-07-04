package com.ppmall.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;
import com.ppmall.util.TokenUtil;

@Controller
@RequestMapping("/api/user")
public class UserApiController {
	@Autowired
	private IUserService iUserService;
	
	@RequestMapping(value = "/wechat_login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse wechatLogin(String code, String encryptedData, String iv, HttpSession session) {
		ServerResponse response = iUserService.wechatLogin(code, encryptedData, iv);
		if (response.isSuccess()) {
			User user = (User) ((Map) response.getData()).get("user");
//			user = new User();
//			httpResponse.addHeader("Authentication", token);
//			session.setAttribute(Const.CURRENT_USER, user);
		}
		return response;
	}
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse login(String username, String password, HttpSession session, HttpServletResponse httpResponse) {
        ServerResponse response = iUserService.login(username, password);
        Map returnMap = new HashMap<>();
        if (response.isSuccess()){
        	Map claims = new HashMap<>();
        	User user =  (User) response.getData();
			claims.put(Const.CURRENT_USER, user);
			
			long now = new Date().getTime();
			
 			String accessToken = TokenUtil.createToken(claims, String.valueOf(user.getId()), now + Const.ExpiredType.ONE_HOUR * 2 );
 			
 			claims.clear();
 			claims.put("userId",user.getId());
 			String refreshToken = TokenUtil.createToken(claims, String.valueOf(user.getId()), now + Const.ExpiredType.ONE_MONTH);

 			returnMap.put("user", user);
 			returnMap.put("accessToken", accessToken);
 			returnMap.put("refreshToken", refreshToken);
 			returnMap.put("expired", now + Const.ExpiredType.ONE_HOUR * 2);
 			
        }
            
        return ServerResponse.createSuccess("登陆成功",returnMap);
			
    }
}
