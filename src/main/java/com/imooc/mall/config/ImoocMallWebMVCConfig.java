package com.imooc.mall.config;

import com.imooc.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImoocMallWebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
//    #生成容器
//    docker run --name nginx -p 9001:80-d nginx
//        #将容器nginx.conf文件复制到宿主机
//    docker cp nginx:/etc/nginx/nginx.conf /home/nginx/conf/nginx.conf
//    #将容器conf.d文件夹下内容复制到宿主机
//    docker cp nginx:/etc/nginx/conf.d /home/nginx/conf/conf.d
//    #将容器中的html文件夹复制到宿主机
//    docker cp nginx:/usr/share/nginx/html /home/nginx/
}
