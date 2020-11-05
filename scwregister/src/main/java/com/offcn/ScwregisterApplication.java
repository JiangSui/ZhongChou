package com.offcn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  //开启服务注册和服务发现服务
public class ScwregisterApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScwregisterApplication.class, args);
	}

}
