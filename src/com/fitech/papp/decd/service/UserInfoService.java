package com.fitech.papp.decd.service;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.model.vo.UserInfoVo;

/**
 * 用户service
 * 
 * @author wupei
 * 
 */
@Service(value = "userInfoService")
public interface UserInfoService {

    public UserInfoVo findUserInfo(String username, String password) throws Exception;

    public boolean addUserInfo(UserInfo userInfo);

    public boolean updateUserInfo(UserInfo userInfo);

    public boolean delUserInfo(UserInfo userInfo);

    public UserInfo findUser(String username) throws Exception;
}
