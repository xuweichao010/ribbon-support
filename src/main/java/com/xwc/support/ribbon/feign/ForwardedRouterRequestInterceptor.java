package com.xwc.support.ribbon.feign;

import com.xwc.support.ribbon.RibbonSupportProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * 类描述：把IP信息传递到下游服务的一个拦截器
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:26
 */
public class ForwardedRouterRequestInterceptor implements RequestInterceptor, Ordered {


    private RibbonSupportProperties ribbonSupportProperties;
    private ServletExtractRouterIp servletExtractRouterIp;

    public ForwardedRouterRequestInterceptor(RibbonSupportProperties ribbonSupportProperties,
                                             ServletExtractRouterIp servletExtractRouterIp) {
        this.servletExtractRouterIp = servletExtractRouterIp;
        this.ribbonSupportProperties = ribbonSupportProperties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RibbonSupportProperties.IpRuleProperties ipRule = ribbonSupportProperties.getIpRule();
        String routerIp = servletExtractRouterIp.get();
        if (StringUtils.hasText(routerIp)) {
            HashMap<String, Collection<String>> headers = new HashMap<>(requestTemplate.headers());
            headers.put(ipRule.getHeadName(), Collections.singleton(routerIp));
            requestTemplate.headers(headers);
        }
    }

    public ServletExtractRouterIp getServletExtractRouterIp() {
        return servletExtractRouterIp;
    }

    public void setServletExtractRouterIp(ServletExtractRouterIp servletExtractRouterIp) {
        this.servletExtractRouterIp = servletExtractRouterIp;
    }

    public RibbonSupportProperties getRibbonSupportProperties() {
        return ribbonSupportProperties;
    }

    public void setRibbonSupportProperties(RibbonSupportProperties ribbonSupportProperties) {
        this.ribbonSupportProperties = ribbonSupportProperties;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
