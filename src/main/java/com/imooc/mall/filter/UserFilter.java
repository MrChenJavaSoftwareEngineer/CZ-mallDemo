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

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class UserFilter implements Filter {

//    public static User currentUser = null;验证成功，而他没有加载完毕

    public static User currentUser =new User();//虽然这个currentUser中所有值都是null，但是其程序都加载完毕了

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException{
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
//            对null的实体对象set时会出现空指针异常时因为实体类序列化后需要创建一个对象,
//            实体类序列化后就是指 User currentUser ;相当于还不是特别完整，有些java内的验证不会通过，会报null错的
//            而实体类序列化后需要创建一个对象时，指的是 User currentUser = new User();表示类已经进行完整的加载了
//            可以认为:User currentUser  实体类序列化后  表示只是加载了静态的程序，而非静态的程序没有进行加载，
//            所以不会通过检查，从而报错。当创建实体类序列化后并赋予创建一个同类型的对象时，表示该类的程序都加载完毕。
//            现在可以简单的认为:java的检查可以认为是对类的加载，null其实也是一个值，但是不是其进行检查的要求。
//            解决方法：new一下就好了
            currentUser.setId(jwt.getClaim(Constant.USER_ID).asInt());
            currentUser.setUsername(jwt.getClaim(Constant.USER_NAME).asString());
            currentUser.setRole(jwt.getClaim(Constant.USER_ROLE).asInt());
        }catch (TokenExpiredException e){
            //token过期,抛出异常
            throw new IOException(new ImoocMallException(ImoocMallExceptionEnum.TOKEN_EXPIRED));
        }catch (JWTDecodeException e){
            //解码失败，抛出异常
            throw new IOException(new ImoocMallException(ImoocMallExceptionEnum.TOKEN_WRONG));
        }
        if (currentUser == null) {
             PrintWriter out = new HttpServletResponseWrapper(
                     (HttpServletResponse)servletResponse).getWriter();
             out.write("{\n"
                     + "    \"status\": 10007,\n"
                     + "    \"msg\": \"NEED_LOGIN\",\n"
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
