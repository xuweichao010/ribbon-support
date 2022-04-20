package com.xwc.support.ribbon.rule;

import com.xwc.support.ribbon.ExtractTargetRouter;
import com.xwc.support.ribbon.IpRibbonProperties;
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
public class IpExtractRouter implements ExtractTargetRouter {

    @Autowired
    private IpRibbonProperties.IpRuleProperties ipRule;

    @Override
    public String get() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String loadIp = null;
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            loadIp = request.getHeader(ipRule.getHeadName());
            if (StringUtils.isEmpty(loadIp)) {
                Object attribute = request.getSession().getAttribute(ipRule.getHeadName());
                loadIp = attribute == null ? null : attribute.toString();
            }
        }
        return loadIp;
    }
}
