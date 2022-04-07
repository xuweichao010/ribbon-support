package com.xwc.ipribbon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * 类描述：IP路由的配置类
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:27
 */

@Data
@ConfigurationProperties(value = "ip-ribbon")
public class IpRibbonProperties {

    public static final String AUTO_CONFIG = "ip-ribbon.rule.enable";

    /**
     * 路由配置
     */
    private IpRuleProperties ipRule = new IpRuleProperties();


    @Data
    public static class IpRuleProperties {

        /**
         * 是否开启 开发路由规则
         */
        private boolean enable = false;

        /**
         * 在没有指定路由的时候，可以使用默认ip 来尝试获取路由，当默认ip也无法获取路由时，才会随机路由 一般用于服务的路由规则
         * 这些规则的优先级是: routerIp -> defaultIp -> randomIp
         */
        private String defaultIp;

        /**
         * 排除自动路由的IP正则, 排除的IP只能通过指定路由(routerIp、defaultIp)来访问，无法通过随机路由(randomIp)来访问
         */
        private Pattern excludeIpRegex;

        private String headName = "RouterIp";

        public void setDefaultIp(String defaultIp) {
            if ("127.0.0.1".equals(defaultIp) || "localhost".equals(defaultIp)) {
                try {
                    InetAddress addr = InetAddress.getLocalHost();
                    this.defaultIp = addr.getHostAddress();
                } catch (Exception e) {
                    this.defaultIp = defaultIp;
                }
            }
        }
    }
}
