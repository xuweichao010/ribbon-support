package com.xwc.support.ribbon.zuul;

import com.netflix.zuul.context.RequestContext;
import com.xwc.support.ribbon.ExtractTargetRouter;
import com.xwc.support.ribbon.RibbonSupportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：抽取路由的目标IP信息
 * 作者：徐卫超 (cc)
 * 时间 2022/4/12 10:03
 */
public class ZuulExtractRouterIp implements ExtractTargetRouter {


    private RibbonSupportProperties ribbonSupportProperties;

    @Override
    public String get() {
        RequestContext context = RequestContext.getCurrentContext();
        String routerIp = context.getRequest().getHeader(ribbonSupportProperties.getIpRule().getHeadName());
        if (StringUtils.hasText(routerIp)) {
            return routerIp;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String header = request.getHeader(ribbonSupportProperties.getIpRule().getHeadName());
            if (StringUtils.hasText(header)) {
                /**
                 * 这里是为了适应在线文档的一种方案，在线文档可以把ip存储在session来帮助实现在swagger上快速调用接口文档
                 */
                return header;
            }
            Object attribute = request.getSession().getAttribute(ribbonSupportProperties.getIpRule().getHeadName());
            if (attribute != null) {
                return String.valueOf(attribute);
            }
        } else if (StringUtils.hasText(ribbonSupportProperties.getIpRule().getDefaultIp())) {
            return ribbonSupportProperties.getIpRule().getDefaultIp();
        }
        return null;
    }

    public ZuulExtractRouterIp(RibbonSupportProperties ribbonSupportProperties) {
        this.ribbonSupportProperties = ribbonSupportProperties;
    }
}
