package com.example.nacos.zuul.gateway.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.example.nacos.zuul.gateway.entity.ZuulRouteEntity;
import com.example.nacos.zuul.gateway.router.NacosDynamicRouteLocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Nacos动态路由监听
 */
@Slf4j
@Component
public class NacosDynamicRouteListener {

    /**
     * Nacos动态路由定位器
     */
    @Autowired
    private NacosDynamicRouteLocator locator;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 变动触发
     * @param configInfo
     */
    @NacosConfigListener(dataId = "${nacos.gateway.dynamic.routeDataId}", groupId = "${nacos.gateway.dynamic.routeGroupId}")
    public void onListener(String configInfo) {
        log.info("Nacos网关路由配置变动, 信息 = {}", configInfo);
        //设置路由信息到定位器
        locator.setZuulRouteEntities(parseRouteJsonToEntity(configInfo));
        //发布zuul路由刷新事件
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(locator);
        publisher.publishEvent(routesRefreshedEvent);
    }

    /***
     * 格式化json
     * @param content
     * @return
     */
    private List<ZuulRouteEntity> parseRouteJsonToEntity(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return JSONObject.parseArray(content, ZuulRouteEntity.class);
        }
        return new ArrayList<>(0);
    }
}
