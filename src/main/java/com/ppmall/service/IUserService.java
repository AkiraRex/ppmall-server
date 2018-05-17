package com.ppmall.service;

import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.Category;
import com.ppmall.pojo.User;

import java.util.List;


public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(String username, String password, String mail, String phone, String question, String answer, int role);

    ServerResponse<String> getPassQuestion(String username);

    ServerResponse<String> checkAnswer(User user);

    ServerResponse<String> resetPasswordByQues(User user);

    ServerResponse<String> resetPasswordByPass(User currentUser, String password, String passwordNew);

    ServerResponse<String> checkValid(String str);

    ServerResponse<String> updateInformation(User user);

    ServerResponse checkAdmin(User user);




}
