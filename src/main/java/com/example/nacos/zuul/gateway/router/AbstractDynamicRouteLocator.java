package com.example.nacos.zuul.gateway.router;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模板方法：重写加载动态路由
 */
public abstract class AbstractDynamicRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    public AbstractDynamicRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routersMap = new LinkedHashMap<>();
        // 从application.properties中加载静态路由信息
        routersMap.putAll(super.locateRoutes());
        // 从Nacos中加载动态路由信息
        routersMap.putAll(loadDynamicRoute());
        // 优化配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routersMap.entrySet()) {
            String path = entry.getKey();
            if (StringUtils.isEmpty(path)) {
                continue;
            }
            values.put(path, entry.getValue());
        }
        return values;

    }

    protected abstract Map<String, ZuulProperties.ZuulRoute> loadDynamicRoute();
}

