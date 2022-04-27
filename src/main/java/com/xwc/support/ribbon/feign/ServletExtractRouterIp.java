package com.xwc.support.ribbon.feign;

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
public class ServletExtractRouterIp implements ExtractTargetRouter {


    private RibbonSupportProperties ribbonSupportProperties;

    public ServletExtractRouterIp(RibbonSupportProperties ribbonSupportProperties) {
        this.ribbonSupportProperties = ribbonSupportProperties;
    }

    @Override
    public String get() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RibbonSupportProperties.IpRuleProperties ipRule = ribbonSupportProperties.getIpRule();
        String loadIp = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            loadIp = request.getHeader(ipRule.getHeadName());
            if (StringUtils.isEmpty(loadIp)) {
                Object attribute = request.getSession().getAttribute(ipRule.getHeadName());
                loadIp = attribute == null ? null : attribute.toString();
            }
        } else if (StringUtils.hasText(ipRule.getDefaultIp())) {
            return ipRule.getDefaultIp();
        }


        return loadIp;
    }
}
