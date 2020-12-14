package com.example.nacos.zuul.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
//启用Zuul代理
@EnableZuulProxy
public class NacosZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosZuulGatewayApplication.class, args);
    }

}
