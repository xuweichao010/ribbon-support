package com.xwc.support.ribbon;

import com.xwc.support.ribbon.feign.ForwardedRouterRequestInterceptor;
import com.xwc.support.ribbon.feign.ServletExtractRouterIp;
import com.xwc.support.ribbon.zuul.RouterIpHeaderWriteFilter;
import com.xwc.support.ribbon.zuul.ZuulExtractRouterIp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：自动化配置ribbon-support的配置类
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:44
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = RibbonSupportConfiguration.AUTO_CONFIG, havingValue = "true")
@RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)
public class RibbonSupportConfiguration {

    public static final String AUTO_CONFIG = "ribbon.support.ipRule.enable";

    @Configuration
    @ConditionalOnClass(name = {"feign.RequestInterceptor", "org.springframework.cloud.netflix.ribbon.RibbonClients"})
    public static class RuleFeignConfig {
        @Bean
        @ConditionalOnMissingBean
        public ForwardedRouterRequestInterceptor forwardedRouterRequestInterceptor(RibbonSupportProperties ribbonSupportProperties, ServletExtractRouterIp servletExtractRouterIp) {
            return new ForwardedRouterRequestInterceptor(ribbonSupportProperties, servletExtractRouterIp);
        }

        @Bean
        @ConditionalOnMissingBean
        public ServletExtractRouterIp servletExtractRouterIp(RibbonSupportProperties ribbonSupportProperties) {
            return new ServletExtractRouterIp(ribbonSupportProperties);
        }
    }

    @Configuration
    @RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)
    @ConditionalOnClass(name = {"org.springframework.cloud.netflix.ribbon.RibbonClients", "com.netflix.zuul.ZuulFilter"})
    public static class RuleZuulConfig {
        @Bean
        @ConditionalOnMissingBean
        public RouterIpHeaderWriteFilter ruleFilter(RibbonSupportProperties ribbonSupportProperties,
                                                    ZuulExtractRouterIp zuulExtractRouterIp) {
            return new RouterIpHeaderWriteFilter(ribbonSupportProperties, zuulExtractRouterIp);
        }

        @Bean
        @ConditionalOnMissingBean
        public ExtractTargetRouter zuulExtractRouterIp(RibbonSupportProperties ribbonSupportProperties) {
            return new ZuulExtractRouterIp(ribbonSupportProperties);
        }
    }

    @Bean
    @ConfigurationProperties(value = "ribbon.support")
    public RibbonSupportProperties ribbonSupportProperties() {
        return new RibbonSupportProperties();
    }


}
