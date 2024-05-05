package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.UserVO;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    void register(String userName, String passWord,String verificationCode) throws ImoocMallException, NoSuchAlgorithmException;


    User login(String userName, String passWord) throws ImoocMallException, NoSuchAlgorithmException;

    void update(String signature) throws ImoocMallException;

    Boolean adminCheck(User user);

    List<UserVO> list();

    boolean checkEmailRegistered(String emailAddress);

    String getVerification() throws ImoocMallException;
}
