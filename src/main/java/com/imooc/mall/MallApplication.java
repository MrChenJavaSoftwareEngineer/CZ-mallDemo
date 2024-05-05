package com.imooc.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.TimeZone;
/*
单体，集群，分布式，微服务
单体：前端和后端部署在一个服务器上，快速进行开发，但是不利于并发量过高的情况，不利于版本进行升级
集群：单体进行复制多份，并部署在多个服务器上，减轻单个服务器的压力，能够更好进行处理request，但是会使代码冗余，不利于版本进行升级
分布式：就是对前后端的系统的拆分，分布式并没有太多的定义，只要是拆开分别进行部署就可以了，利于版本进行升级，代码冗余解决了
微服务：就是分布式的细化，根据服务层进行拆分，如订单和会员分别进行部署在不同服务器上，这些都是分布式，但是更加细化，更利于版本的进行升级，
代码冗余更是不太可能，更加有效进行处理request

CAP定理
C->Consistency：表示一致性，比如数据要进行同步到不同的服务器上
A->Availability：表示可用性，比如不管数据同不同步，只要有request，必然会有response返回
P->PartitionTolerance：表示分区容错性，比如服务器不在同一个地区，一个在欧洲，一个在美洲，由于网络是进行分区的，由于网络是不可靠的，
可能导致所有服务器节点之间出现无法通讯的情况，在节点不能通信时，要保证系统可以继续正常服务。
 */

@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mall.model.dao")
@EnableSwagger2
@EnableCaching
public class MallApplication {
    public static void main(String[] args) {
        System.out.println(TimeZone.getDefault());
        SpringApplication.run(MallApplication.class,args);
    }
}
