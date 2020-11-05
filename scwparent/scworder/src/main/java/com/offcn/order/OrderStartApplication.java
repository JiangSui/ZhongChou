package com.offcn.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //eureka 客户端
@MapperScan("com.offcn.order.mapper")
@EnableCircuitBreaker //开启熔断
@EnableFeignClients
public class OrderStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderStartApplication.class,args);
    }
}
