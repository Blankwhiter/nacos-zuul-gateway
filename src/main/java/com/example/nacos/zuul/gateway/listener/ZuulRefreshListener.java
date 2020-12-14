package com.example.nacos.zuul.gateway.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * zuul刷新监听
 */
@Component
@Slf4j
public class ZuulRefreshListener implements ApplicationListener<ApplicationEvent> {

    /**
     * 应用刷新事件
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent
                || event instanceof RefreshScopeRefreshedEvent
                || event instanceof RoutesRefreshedEvent
                || event instanceof InstanceRegisteredEvent) {
            Object source = event.getSource();
            reset(source);
        }
    }


    private void reset(Object locator) {
        log.debug("ZuulRefreshListener 监听到更新");
    }
}


