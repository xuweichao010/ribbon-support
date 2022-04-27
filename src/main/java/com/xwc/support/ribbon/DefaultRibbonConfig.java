package com.xwc.support.ribbon;

import com.netflix.loadbalancer.IRule;
import com.xwc.support.ribbon.feign.ServletExtractRouterIp;
import com.xwc.support.ribbon.rule.IpRule;
import org.springframework.context.annotation.Bean;

/**
 * 类描述：定义自定义负载的配置类
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:23
 */
public class DefaultRibbonConfig {


    @Bean
    public IRule ipRibbonRule(ServletExtractRouterIp servletExtractRouterIp,
                              RibbonSupportProperties ribbonSupportProperties) {
        return new IpRule(ribbonSupportProperties, servletExtractRouterIp);
    }

}
