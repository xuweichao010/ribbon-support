package com.xwc.support.ribbon;

import com.xwc.support.ribbon.feign.IpRequestInterceptor;
import com.xwc.support.ribbon.zuul.IpRuleFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(name = "org.springframework.cloud.netflix.ribbon.RibbonClients")
public class RibbonConfiguration {

    public static final String AUTO_CONFIG = "ribbon.support.ipRule.enable";

    @Configuration
    @ConditionalOnProperty(value = AUTO_CONFIG, havingValue = "true")
    @RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    public static class RuleFeignConfig {
        @Bean
        public IpRequestInterceptor devCommonRequestInterceptor() {
            return new IpRequestInterceptor();
        }
    }


    @Configuration
    @ConditionalOnProperty(value = AUTO_CONFIG, havingValue = "true")
    @RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)
    @ConditionalOnClass(name = "com.netflix.zuul.ZuulFilter")
    public static class RuleZuulConfig {
        @Bean
        public IpRuleFilter ruleFilter() {
            return new IpRuleFilter();
        }
    }


    @Bean
    @ConfigurationProperties(value = "ribbon.support")
    public IpRibbonProperties developerProperties() {
        return new IpRibbonProperties();
    }


}
