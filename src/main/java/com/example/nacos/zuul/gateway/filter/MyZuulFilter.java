package com.example.nacos.zuul.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义网关过滤器
 */
@Component
public class MyZuulFilter extends ZuulFilter {
    /**
     * 过滤类型
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 拦截级别 越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否进行拦截
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        System.out.println(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        System.out.println((String.format("LocalAddr: %s", request.getLocalAddr())));
        System.out.println((String.format("LocalName: %s", request.getLocalName())));
        System.out.println((String.format("LocalPort: %s", request.getLocalPort())));
        System.out.println((String.format("RemoteAddr: %s", request.getRemoteAddr())));
        System.out.println((String.format("RemoteHost: %s", request.getRemoteHost())));
        System.out.println((String.format("RemotePort: %s", request.getRemotePort())));

//根据自身需求进行过滤信息处理
//        Object accessToken = request.getParameter("token");
//        if (accessToken == null) {
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(401);
//            try {
//                context.getResponse().setHeader("Content-Type", "text/html;charset=UTF-8");
//                context.getResponse().getWriter().write("登录信息为空！");
//            } catch (Exception e) {
//            }
//            return null;
//        }
        return null;
    }
}
