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
import com.ppmall.common.Const.ExpiredType;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.User;
import com.ppmall.service.ITokenService;
import com.ppmall.service.IUserService;
import com.ppmall.util.TokenUtil;

@Controller
@RequestMapping("/api/user")
public class UserApiController {
	@Autowired
	private IUserService iUserService;
	
	@RequestMapping(value = "/wechat_login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse wechatLogin(String code, String encryptedData, String iv, HttpSession session, HttpServletResponse httpResponse) {
		ServerResponse response = iUserService.wechatLogin(code, encryptedData, iv);
		if (response.isSuccess()) {
			User user = (User) ((Map) response.getData()).get("user");
			Map claims = new HashMap<>();
			claims.put(Const.CURRENT_USER, user);
 			String token = TokenUtil.createToken(claims, String.valueOf(user.getId()), Const.ExpiredType.ONE_MONTH );
 			httpResponse.addHeader("Authentication", token);
//			session.setAttribute(Const.CURRENT_USER, user);
		}
		return response;
	}
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse login(String username, String password, HttpSession session, HttpServletResponse httpResponse) {
        ServerResponse response = iUserService.login(username, password);
        if (response.isSuccess()){
        	Map claims = new HashMap<>();
        	User user =  (User) response.getData();
			claims.put(Const.CURRENT_USER, user);
 			String token = TokenUtil.createToken(claims, String.valueOf(user.getId()), new Date().getTime() + ExpiredType.ONE_MONTH );
 			httpResponse.addHeader("Authentication", token);
// 			session.setAttribute(Const.CURRENT_USER, user);
        }
            
        return response;
    }
}
