package com.example.nacos.zuul.gateway.register;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 使用CommandLineRunner，项目启动后执行往nacos中心注册服务任务
 */
@Component
public class CommandLineNacosRegisterRunner implements CommandLineRunner {

    @Value("${nacos.ip:127.0.0.1}")
    private String nacosIp;

    @NacosInjected
    private NamingService namingService;

    @Resource
    private NacosRegisterProperties nacosProperties;

    @Override
    public void run(String... args) throws Exception {
        namingService.registerInstance(nacosProperties.getApplicationName(), nacosProperties.getGroupName(), nacosIp, nacosProperties.getServerPort());
    }
}
