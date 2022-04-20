package com.xwc.support.ribbon.feign;

import com.xwc.support.ribbon.IpRibbonProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：把IP信息传递到下游服务的一个拦截器
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:26
 */
public class IpRequestInterceptor implements RequestInterceptor, Ordered {

    @Autowired
    private IpRibbonProperties ipRibbonProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        IpRibbonProperties.IpRuleProperties ipRule = ipRibbonProperties.getIpRule();
        // 尝试从上游服务取出路由信息
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String loadIp = request.getHeader(ipRule.getHeadName());
            if (StringUtils.hasText(loadIp)) {
                HashMap<String, Collection<String>> headers = new HashMap<>(requestTemplate.headers());
                headers.put(ipRule.getHeadName(), Collections.singleton(loadIp));
                requestTemplate.headers(headers);
            }
        }
        //当发现没有上游服务 那就判断当前节点是否有默认路由配置 当有路由配置的就使用当前的路由
        else if (StringUtils.hasText(ipRule.getDefaultIp())) {
            Map<String, Collection<String>> headersMap = requestTemplate.headers();
            if (headersMap.containsKey(ipRule.getHeadName())) {
                HashMap<String, Collection<String>> headers = new HashMap<>(requestTemplate.headers());
                headers.put(ipRule.getHeadName(), Collections.singleton(ipRule.getDefaultIp()));
                requestTemplate.headers(headers);
            }
        }
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
