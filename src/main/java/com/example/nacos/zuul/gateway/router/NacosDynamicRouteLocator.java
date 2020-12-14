package com.example.nacos.zuul.gateway.router;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.example.nacos.zuul.gateway.entity.ZuulRouteEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Nacos动态路由定位器
 */
@Slf4j
public class NacosDynamicRouteLocator extends AbstractDynamicRouteLocator {

    private ConfigService nacosConfigService;
    /**
     * Nacos的dataId名称
     */
    private String dataId;
    /**
     * Nacos的groupId名称
     */
    private String groupId;

    private List<ZuulRouteEntity> zuulRouteEntities;

    public void setZuulRouteEntities(List<ZuulRouteEntity> zuulRouteEntities) {
        this.zuulRouteEntities = zuulRouteEntities;
    }

    public NacosDynamicRouteLocator(ConfigService nacosConfigService,
                                    String servletPath, ZuulProperties properties,
                                    String dataId, String groupId) {
        super(servletPath, properties);
        this.nacosConfigService = nacosConfigService;
        this.dataId = dataId;
        this.groupId = groupId;
    }

    /**
     * 服务启动时动态加载Nacos配置
     */
    @Override
    protected Map<String, ZuulProperties.ZuulRoute> loadDynamicRoute() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        if (zuulRouteEntities == null) {
            log.debug("zuulRouteEntities 配置为空 ");
            zuulRouteEntities = pullNacosConfig();
        }
        try {
            for (ZuulRouteEntity result : zuulRouteEntities) {
                if (StringUtils.isBlank(result.getPath()) || !result.isEnabled()) {
                    continue;
                }
                ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
                BeanUtils.copyProperties(result, zuulRoute);
                routes.put(zuulRoute.getPath(), zuulRoute);
            }

            log.info("加载动态路由大小 = {}", routes.size());

        } catch (Exception e) {
            log.error("加载动态路由错误，错误信息 = {}", e.getMessage(), e);
        }
        return routes;
    }

    /**
     * 查询Nacos-zuul的路由配置
     */
    private List<ZuulRouteEntity> pullNacosConfig() {
        try {
            String content = nacosConfigService.getConfig(dataId, groupId, 5000);
            log.info("拉取远程Nacos中的路由信息 = {}", content);
            return parseRouteJsonToEntity(content);
        } catch (NacosException e) {
            log.error("监听Nacos配置发生错误,错误信息 = {}", e.getMessage(), e);
        }
        return new ArrayList<>(0);
    }

    /**
     * JSON格式化数组
     * @param content
     * @return
     */
    public List<ZuulRouteEntity> parseRouteJsonToEntity(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return JSONObject.parseArray(content, ZuulRouteEntity.class);
        }
        return new ArrayList<>(0);
    }
}
