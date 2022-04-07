package com.xwc.ipribbon.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xwc.ipribbon.IpRibbonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：网关的路由传递实现
 * 作者：徐卫超 (cc)
 * 时间 2022/4/7 9:53
 */
public class IpRuleFilter extends ZuulFilter {


    @Autowired
    private IpRibbonProperties ipRibbonProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        if (StringUtils.hasText(context.getRequest().getHeader(ipRibbonProperties.getIpRule().getHeadName()))) {
            return null;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        HttpServletRequest request = requestAttributes.getRequest();
        String header = request.getHeader(ipRibbonProperties.getIpRule().getHeadName());
        if (StringUtils.isEmpty(header)) {
            /**
             * 这里是为了适应在线文档的一种方案，在线文档可以把ip存储在session来帮助实现在swagger上快速调用接口文档
             */
            Object attribute = request.getSession().getAttribute(ipRibbonProperties.getIpRule().getHeadName());
            if (attribute != null) {
                context.addZuulRequestHeader(ipRibbonProperties.getIpRule().getHeadName(), attribute.toString());
            }
        }
        return null;
    }
}
