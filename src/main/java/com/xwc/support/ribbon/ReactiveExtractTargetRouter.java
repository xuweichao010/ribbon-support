package com.xwc.support.ribbon;

import org.springframework.web.server.ServerWebExchange;

/**
 * 类描述：用于抽取目标路由的信息
 * 作者：徐卫超 (cc)
 * 时间 2022/4/12 10:01
 */
public interface ReactiveExtractTargetRouter {

    Object get(ServerWebExchange exchange);
}
