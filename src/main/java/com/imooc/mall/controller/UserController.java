package com.imooc.mall.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.UserVO;
import com.imooc.mall.service.EmailService;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.EmailUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
/*
Get一般是用于获取信息的，如list
Post一般是用于修改信息的，如update
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;
    String token=null;

    @ApiOperation("用户进行注册的")
    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("passWord") String passWord,
                                    @RequestParam("verificationCode") String verificationCode) throws ImoocMallException, NoSuchAlgorithmException {
        userService.register(userName, passWord,verificationCode);
        return ApiRestResponse.success();
    }



    @ApiOperation("用户进行登录的")
    @PostMapping("/login")
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("passWord") String passWord/*,HttpSession session*/) throws ImoocMallException, NoSuchAlgorithmException {
        User user = userService.login(userName, passWord);
        user.setPassword(null);
        //用户密码不能写入session中
//        session.setAttribute(Constant.IMOOC_MALL_USER, user);

        //进行jwt的封装，其中的签名就是一个算法运算出来的结果，algorithm表示算法工具
         Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
         token= JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                //过期的时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
    }

    @ApiOperation("用户进行更新个人签名的")
    @PostMapping("/user/update")
    public ApiRestResponse update(@RequestParam String sigNature) throws ImoocMallException {
        userService.update(sigNature);
        return ApiRestResponse.success();
    }

    @ApiOperation("用户进行登出的")
    @PostMapping("/user/logout")
    public ApiRestResponse out() {
        //返回一个null，前端进行处理。
        return ApiRestResponse.success();
    }

    @ApiOperation("管理员进行登录的")
    @PostMapping("/adminLogin")
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                      @RequestParam("passWord") String passWord/*,
                                     HttpSession session*/)
            throws ImoocMallException, NoSuchAlgorithmException {
        User user = userService.login(userName, passWord);
        user.setPassword(null);
//        session.setAttribute(Constant.IMOOC_MALL_USER, user);

        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis()+Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
    }

    @ApiOperation("管理员和用户的列表")
    @GetMapping("/admin/userAndAdmin/list")
    public ApiRestResponse userAndAdminList() {
        List<UserVO> list = userService.list();
        return ApiRestResponse.success(list);
    }

    //注册时,要发送邮件
    @ApiOperation("注册时,要发送邮件")
    @PostMapping("/sendEmail")
    public ApiRestResponse sendEmail(@RequestParam("emailAddress") String emailAddress)
            throws ImoocMallException, NoSuchAlgorithmException {
        //检查邮件地址是否有效，检查是否已注册
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if (validEmailAddress) {
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if (!emailPassed) {
                return ApiRestResponse.error(ImoocMallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
            } else {
                String verificationCode = userService.getVerification();

                     emailService.sendSimpleMessage(emailAddress, Constant.EMAIL_SUBJECT,
                             "欢迎注册,您的验证码是:"+verificationCode);
                     return ApiRestResponse.success();

            }
        } else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.WRONG_EMAIL);
        }
    }
}
