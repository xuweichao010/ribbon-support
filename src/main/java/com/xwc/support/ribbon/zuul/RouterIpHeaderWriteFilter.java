package com.xwc.support.ribbon.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xwc.support.ribbon.ExtractTargetRouter;
import com.xwc.support.ribbon.RibbonSupportProperties;
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
public class RouterIpHeaderWriteFilter extends ZuulFilter {

    private RibbonSupportProperties ribbonSupportProperties;
    private ExtractTargetRouter extractTargetRouter;

    public RouterIpHeaderWriteFilter(RibbonSupportProperties ribbonSupportProperties, ExtractTargetRouter extractTargetRouter) {
        this.ribbonSupportProperties = ribbonSupportProperties;
        this.extractTargetRouter = extractTargetRouter;
    }

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
        String header = extractTargetRouter.get();
        if (StringUtils.isEmpty(header)) {
            context.addZuulRequestHeader(ribbonSupportProperties.getIpRule().getHeadName(), header);
        }
        return null;
    }
}
