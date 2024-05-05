package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.UserVO;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.EmailUtil;
import com.imooc.mall.util.MD5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static String verificationCode;
    private static long start;

    @Autowired
    UserMapper userMapper;
@Override
    public void register(String userName, String passWord,String verification) throws ImoocMallException, NoSuchAlgorithmException {
    long end = System.currentTimeMillis();
    //用户名不能重复
     User result = userMapper.selectByName(userName);
    if (result != null) {
        throw new ImoocMallException(ImoocMallExceptionEnum.USER_EXISTED);
    }
    //检验验证码
    if (!verificationCode.equals(verification)){
        throw new ImoocMallException(ImoocMallExceptionEnum.CODE_ERROR);
    }
    //验证码时间的期限的检验
    if ((end-start)>(60*1000)){
        throw new ImoocMallException(ImoocMallExceptionEnum.CODE_ERROR);
    }
    //写入数据库
    if (StringUtils.isEmpty(userName)){
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(passWord)){
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_USER_PASSWORD);
        }
        if (passWord.length()<=8){
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_PASSWORD_SHORT);
        }
        User user = new User();
        user.setUsername(userName);
        user.setPassword(MD5Utils.getMD5Str(passWord));
         int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);

        }
    }


    @Override
    public User login(String userName, String passWord) throws ImoocMallException, NoSuchAlgorithmException {
        //判断
        if (StringUtils.isEmpty(userName)){
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(passWord)){
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_USER_PASSWORD);
        }
        if (passWord.length()<=8){
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_PASSWORD_SHORT);
        }
        //查询用户是否存在

        User result = userMapper.selectByName(userName);
        if (result==null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_USER_REGISTER);
        }
        //密码进行MD5化
        passWord=MD5Utils.getMD5Str(passWord);
        if (!result.getPassword().equals(passWord)){
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_PASSWORD_ERROR);
        }
        return result;
    }
    @Override
    public void update(String signature) throws ImoocMallException {
         User result = userMapper.selectByName(UserFilter.currentUser.getUsername());
        if (result == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        result.setPersonalizedSignature(signature);
         User user = new User();
        BeanUtils.copyProperties(result,user);
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count==0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }

    }
    @Override
    public Boolean adminCheck(User user){
    return user.getRole().equals(2);
    }

    @Override
    public List<UserVO> list() {
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userMapper.selectList()) {
             UserVO userVO = new UserVO();
             BeanUtils.copyProperties(user,userVO);
             userVOList.add(userVO);
        }
        return userVOList;
    }

    @Override
    public boolean checkEmailRegistered(String emailAddress) {
        User user = userMapper.selectOneByEmailAddress(emailAddress);
        if (user != null) {
            return false;
        }
        return true;
    }

    @Override
    public String getVerification() throws ImoocMallException {
        verificationCode = EmailUtil.genVerificationCode();
        if (verificationCode==null){
            throw new ImoocMallException(ImoocMallExceptionEnum.CODE_ERROR);
        }
        start = System.currentTimeMillis();
        return verificationCode;
    }
}
