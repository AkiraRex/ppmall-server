package com.ppmall.service.impl;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.AuthMapper;
import com.ppmall.dao.UserMapper;
import com.ppmall.pojo.Auth;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;
import com.ppmall.util.AesUtil;
import com.ppmall.util.DateUtil;
import com.ppmall.util.HttpUtil;
import com.ppmall.util.JsonUtil;
import com.ppmall.util.MD5Util;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.util.UUIDUtil;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private AuthMapper authMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		User user = userMapper.loginByUsernameAndPassword(username, MD5Util.MD5EncodeUtf8(password));
		if (user == null) {
			return ServerResponse.createErrorMessage("用户名或密码错误");
		}
		// todo 密碼MD5
		return ServerResponse.createSuccess("登陆成功", user);

	}

	@Override
	public ServerResponse<String> register(String username, String password, String mail, String phone, String question,
			String answer, int role) {
		int userCount = userMapper.selectCountByUsername(username);
		if (userCount > 0)
			return ServerResponse.createErrorMessage("用户名已存在");

		int phoneCount = userMapper.selectCountByPhone(phone);
		if (phoneCount > 0)
			return ServerResponse.createErrorMessage("该手机号码已注册");

		int emailCount = userMapper.selectCountByEmail(mail);
		if (emailCount > 0)
			return ServerResponse.createErrorMessage("该邮箱已注册");

		User newUser = new User();

		newUser.setUsername(username);
		newUser.setPassword(MD5Util.MD5EncodeUtf8(password));

		newUser.setAnswer(answer);
		newUser.setQuestion(question);

		newUser.setRole(Const.Role.ROLE_CUSTOMER);
		newUser.setPhone(phone);
		newUser.setEmail(mail);

		Date createTime = DateUtil.getDate();
		newUser.setCreateTime(createTime);
		newUser.setUpdateTime(createTime);

		int insertCount = userMapper.insert(newUser);

		if (insertCount > 0) {
			return ServerResponse.createSuccessMessage("注册成功");
		} else {
			return ServerResponse.createErrorMessage("注册失败");
		}

	}

	@Override
	public ServerResponse<String> getPassQuestion(String username) {
		String question = userMapper.selectPassQuestionByUsername(username);
		if (question != null) {
			return ServerResponse.createSuccess(question);
		}
		return ServerResponse.createErrorMessage("获取密码保护问题失败");
	}

	@Override
	public ServerResponse<String> checkAnswer(User user) {
		String answer = userMapper.selectAnswerByUsername(user.getUsername());

		if (answer.equals(user.getAnswer())) {
			String forgetToken = UUIDUtil.getUUID();
			return ServerResponse.createSuccess("答案正确", forgetToken);
		}
		return ServerResponse.createErrorMessage("答案错误");
	}

	@Override
	public ServerResponse<String> resetPasswordByQues(User user) {
		int updateCount = userMapper.updatePasswordByUsername(user.getUsername(),
				MD5Util.MD5EncodeUtf8(user.getPassword()));

		if (updateCount > 0) {
			return ServerResponse.createSuccess("密码重置成功");
		}

		return ServerResponse.createErrorMessage("问题答案错误");
	}

	@Override
	public ServerResponse<String> resetPasswordByPass(User currentUser, String password, String passwordNew) {
		String userPassword = userMapper.selectPasswordByUsername(currentUser.getUsername());

		if (!userPassword.equals(MD5Util.MD5EncodeUtf8(password))) {
			return ServerResponse.createErrorMessage("原密码错误");
		}

		currentUser.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
		int updateCount = userMapper.updateByPrimaryKeySelective(currentUser);

		if (updateCount > 0) {
			return ServerResponse.createSuccessMessage("修改成功");
		}
		return ServerResponse.createErrorMessage("修改失败");
	}

	@Override
	public ServerResponse<String> checkValid(String str) {
		int userCount = userMapper.selectCountByUsername(str);
		int phoneCount = userMapper.selectCountByPhone(str);
		int mailCount = userMapper.selectCountByEmail(str);

		if (userCount > 0)
			return ServerResponse.createErrorMessage("用户名已存在");
		if (phoneCount > 0)
			return ServerResponse.createErrorMessage("手机号已注册");
		if (mailCount > 0)
			return ServerResponse.createErrorMessage("邮箱已注册");

		return ServerResponse.createSuccess();

	}

	@Override
	public ServerResponse<String> updateInformation(User user) {
		int updateCount = userMapper.updateUserByUsername(user);

		if (updateCount > 0) {
			return ServerResponse.createSuccessMessage("修改成功");
		}
		return ServerResponse.createErrorMessage("修改失败");
	}

	@Override
	public ServerResponse checkAdmin(User user) {
		if (user.getRole() == Const.Role.ROLE_ADMIN) {
			return ServerResponse.createSuccess();
		}
		return ServerResponse.createSuccessMessage("需要管理员权限");
	}

	@Override
	@Transactional
	public ServerResponse wechatLogin(String code, String encryptedData, String iv) {
		// TODO Auto-generated method stub
		String url = PropertiesUtil.getProperty("wechat.login.api");
		String grant_type = PropertiesUtil.getProperty("wechat.login.grant_type");
		String appid = PropertiesUtil.getProperty("wechat.login.appid");
		String secret = PropertiesUtil.getProperty("wechat.login.secret");
		String param = // opa3b4mhz8FavLLS_zsIEsxe2l68
				"?grant_type=" + grant_type 
				+ "&appid=" + appid 
				+ "&secret=" + secret 
				+ "&js_code=" + code;
		Map resultMap = JsonUtil.jsonStringToObject(HttpUtil.httpsGet(url + param), HashMap.class);
		String openid = (String) resultMap.get("openid");
		if (openid != null) {
			User user = userMapper.selectByWechatOpenId(openid);
			String contentString = null;
			Auth auth = null;
			Date now = new Date();
			if (user == null) {
				try {					
					byte contentByte[] = AesUtil.decrypt(Base64.decodeBase64(encryptedData),
							Base64.decodeBase64((String) resultMap.get("session_key")), Base64.decodeBase64(iv));
					contentString = new String(contentByte, "UTF-8");
					
					int end = contentString.indexOf("watermark") - 1;
					contentString = contentString.substring(0, end) + "\"createTime\":\"" + now.getTime() + "\","
																	+ "\"updateTime\":\"" + now.getTime() + "\"}";
																	//+ "\"openid\":\"" + openid + "\"}";
					auth = JsonUtil.jsonStringToObject(contentString, Auth.class);
					
					user = new User();
					user.setWechatOpenid(openid);
					user.setUsername(UUIDUtil.getUUID().substring(0, 11));
					user.setPassword(MD5Util.MD5EncodeUtf8(UUIDUtil.getUUID().substring(0, 6)));
					user.setRole(Const.Role.ROLE_CUSTOMER);
					user.setCreateTime(now);
					user.setUpdateTime(now);
					
					
					userMapper.insert(user);
					authMapper.insert(auth);	
					
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return ServerResponse.createErrorMessage("登陆失败");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return ServerResponse.createErrorMessage("登陆失败");
				}
			} else {
				auth = authMapper.selectByOpenId(openid);
			}
			Map returnMap = new HashMap<>();
			returnMap.put("user", user);
			returnMap.put("auth",auth);
			return ServerResponse.createSuccess("登陆成功", returnMap);
		}

		return ServerResponse.createErrorMessage("登陆失败");
	}
}
