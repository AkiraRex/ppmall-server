package com.ppmall.service.impl;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.UserMapper;
import com.ppmall.pojo.User;
import com.ppmall.service.IUserService;
import com.ppmall.util.DateUtil;
import com.ppmall.util.MD5Util;
import com.ppmall.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;


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
    public ServerResponse<String> register(String username, String password, String mail, String phone, String question, String answer, int role) {
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
        int updateCount = userMapper.updatePasswordByUsername(user.getUsername(), MD5Util.MD5EncodeUtf8(user.getPassword()));

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
}
