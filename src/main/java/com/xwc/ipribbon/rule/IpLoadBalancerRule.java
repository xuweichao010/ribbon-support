package com.xwc.ipribbon.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import com.xwc.ipribbon.IpRibbonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：IP路由规则
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:25
 */
public class IpLoadBalancerRule extends RandomRule {


    @Autowired
    private IpRibbonProperties.IpRuleProperties ipRule;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }


    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loadIp = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            loadIp = request.getHeader(ipRule.getHeadName());
            if (StringUtils.isEmpty(loadIp)) {
                Object attribute = request.getSession().getAttribute(ipRule.getHeadName());
                loadIp = attribute == null ? null : attribute.toString();
            }
        } else if (StringUtils.hasText(ipRule.getDefaultIp())) {
            loadIp = ipRule.getDefaultIp();
        }
        if (StringUtils.hasText(loadIp)) {
            for (Server node : lb.getReachableServers()) {
                if (node.getHost().equals(loadIp) && node.isAlive()) {
                    if (Thread.interrupted()) {
                        return null;
                    }
                    return node;
                }
            }
        }
        if (ipRule.getExcludeIpRegex() != null) {
            return chooseNotExcludesIp(lb, key);
        } else {
            return super.choose(lb, key);
        }
    }

    /**
     * Randomly choose from all living servers
     */
    public Server chooseNotExcludesIp(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = new ArrayList<>();
            List<Server> allList = new ArrayList<>();
            boolean isMatch = false;
            // 获取有效的upService
            for (Server upService : lb.getReachableServers()) {
                if (!ipRule.getExcludeIpRegex().matcher(upService.getHost()).find()) {
                    upList.add(upService);
                }
            }
            // 获取有效的AllService
            for (Server allService : lb.getAllServers()) {
                if (!ipRule.getExcludeIpRegex().matcher(allService.getHost()).find()) {
                    allList.add(allService);
                }
            }

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }

            int index = chooseRandomInt(serverCount);
            server = upList.get(index);

            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;

    }
}
