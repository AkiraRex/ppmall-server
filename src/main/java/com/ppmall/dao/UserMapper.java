package com.ppmall.dao;

import com.ppmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User loginByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    int selectCountByUsername(String username);

    int selectCountByPhone(String phone);

    int selectCountByEmail(String email);

    String selectPassQuestionByUsername(String username);

    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    String selectAnswerByUsername(String username);

    int updateUserByUsername(User user);

    String selectPasswordByUsername(String username);
}