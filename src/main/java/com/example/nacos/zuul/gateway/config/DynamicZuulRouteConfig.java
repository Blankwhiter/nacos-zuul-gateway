package com.example.nacos.zuul.gateway.config;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.example.nacos.zuul.gateway.router.NacosDynamicRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态Zuul路由配置
 */
@Configuration
@ConditionalOnProperty(prefix = "nacos.gateway.dynamic.route", name = "enabled", havingValue = "true")
public class DynamicZuulRouteConfig {

    /**
     * zuul配置属性
     */
    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private DispatcherServletPath dispatcherServletPath;

    /**
     * NacosZuul路由配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "nacos.gateway.dynamic.route", name = "dataType", havingValue = "nacos", matchIfMissing = true)
    public class NacosZuulRoute {

        @NacosInjected
        private ConfigService nacosConfigService;

        @Value("${nacos.gateway.dynamic.routeDataId}")
        private String routeDataId;

        @Value("${nacos.gateway.dynamic.routeGroupId}")
        private String routeGroupId;

        /**
         * 定位器
         * @return
         */
        @Bean
        public NacosDynamicRouteLocator nacosDynRouteLocator() {
            return new NacosDynamicRouteLocator(nacosConfigService, dispatcherServletPath.getPrefix(), zuulProperties, routeDataId, routeGroupId);
        }
    }
}
