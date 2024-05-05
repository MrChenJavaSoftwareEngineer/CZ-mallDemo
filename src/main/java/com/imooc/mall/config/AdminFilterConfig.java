package com.imooc.mall.config;

import com.imooc.mall.filter.AdminFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置文件要有配置注解
@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter AdminFilter(){
        return new AdminFilter();
    }
    @Bean(name="AdminFilterConf")
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(AdminFilter());
        filterRegistrationBean.addUrlPatterns("/admin/*");
        filterRegistrationBean.setName("AdminFilterConf");
        return filterRegistrationBean;
    }
}
