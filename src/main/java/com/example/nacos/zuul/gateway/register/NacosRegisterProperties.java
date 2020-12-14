package com.example.nacos.zuul.gateway.register;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Nacos注册中心配置属性
 */
@Data
@Component
public class NacosRegisterProperties {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int serverPort;

    @Value("${nacos.application.group.name}")
    private String groupName;
}
