package com.offcn.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//启动类
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.user.mapper")  //扫描mapper 映射文件
public class UserStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserStartApplication.class,args);
    }
}
