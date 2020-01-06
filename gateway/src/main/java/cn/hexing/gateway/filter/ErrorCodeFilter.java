package com.cheetah.gateway.filter;

import com.cheetah.common.web.annotation.IgnoreErrorCode;
import com.cheetah.common.web.util.RequestHeaderConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 错误码过滤器，迁移自8.0 com.cheetah.Platform.CommonService.Interceptor.AuthorityInterceptor
 * @author cheetah.zsy
 */
@Component
@Order(1)
public class ErrorCodeFilter implements GlobalFilter {
    private final RequestMappingHandlerMapping handlerMapping;

    public ErrorCodeFilter(
            RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /// 无法完整迁移原有的拦截器功能，具体方法的 annotation 无法获取，暂时不做
//        HandlerMethod handlerMethod = (HandlerMethod) this.handlerMapping.getHandlerInternal(exchange).toProcessor().peek();
//        IgnoreErrorCode ignoreErrorCode = handlerMethod.getMethodAnnotation(IgnoreErrorCode.class);
//        if(ignoreErrorCode != null) {
//            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
//            builder.header(RequestHeaderConstants.IGNORE_ERROR_CODE_KEY, "true");
//            return chain.filter(exchange.mutate().request(builder.build()).build());
//        }
        return chain.filter(exchange);
    }
}
