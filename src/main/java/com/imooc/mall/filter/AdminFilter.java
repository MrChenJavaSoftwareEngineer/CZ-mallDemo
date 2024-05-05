package com.imooc.mall.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminFilter implements Filter {

    public static User currentAdmin =new User();//虽然这个currentUser中所有值都是null，但是其程序都加载完毕了

    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader(Constant.JWT_TOKEN);
        //通过设置好的jwt_key进行获取同样的算法工具
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        //通过算法工具获取解析这种的算法工具,build表示构建这种解析工具
            JWTVerifier verifier = JWT.require(algorithm).build();
        //verify可以理解为这种解析工具的方法，jwt表示已经解析完成了,
        //注意：jwt的储存方式是以key和Value的形式
        try {
            DecodedJWT jwt = verifier.verify(token);
            currentAdmin.setId(jwt.getClaim(Constant.USER_ID).asInt());
            currentAdmin.setUsername(jwt.getClaim(Constant.USER_NAME).asString());
            currentAdmin.setRole(jwt.getClaim(Constant.USER_ROLE).asInt());
        }catch (TokenExpiredException e){
            //token过期,抛出异常
            throw new IOException(new ImoocMallException(ImoocMallExceptionEnum.TOKEN_EXPIRED));
        }catch (JWTDecodeException e){
            //解码失败，抛出异常
            throw new IOException(new ImoocMallException(ImoocMallExceptionEnum.TOKEN_WRONG));
        }
        //查数据是否存在
        if (currentAdmin == null) {
             PrintWriter out = new HttpServletResponseWrapper(
                    (HttpServletResponse) servletResponse).getWriter();
             out.write(
                     "{\n"
                             + "    \"status\": 10008,\n"
                             + "    \"msg\": \"NEED_LOGIN\",\n"
                             + "    \"data\": null\n"
                             + "}"
             );
             out.flush();
             out.close();
             return;
        }
        //判断是否为管理员
        if (!userService.adminCheck(currentAdmin)) {
            PrintWriter out = new HttpServletResponseWrapper(
                    (HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10009,\n"
                    + "    \"msg\": \"NEED_ADMIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
