package com.cheetah.gateway.filter;

import com.cheetah.common.web.annotation.IgnoreSecurity;
import com.cheetah.common.web.util.RequestHeaderConstants;
import com.cheetah.gateway.service.UAPAuthor;
import com.cheetah.gateway.util.UAPUtils;
import com.hexing.uap.common.authorization.AuthorizationInfo;
import com.hexing.uap.common.bean.ModelResponse;
import com.hexing.uap.common.message.TokenResponseCode;
import com.hexing.uap.common.session.SessionUser;
import com.hexing.uap.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 登录状态过滤器，迁移自8.0 com.cheetah.Platform.CommonService.Interceptor.AuthorityInterceptor
 * @author cheetah.zsy
 */
@Component
@Order(2)
public class LoginStatusFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(LoginStatusFilter.class);
    /**
     * 响应 header key，在响应头中暴露权限控制相关信息，header 值为暴露的header key 名称
     */
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    private final RequestMappingHandlerMapping handlerMapping;

    private final UAPAuthor uapAuthor;

    public LoginStatusFilter(
            RequestMappingHandlerMapping handlerMapping, UAPAuthor uapAuthor) {
        this.handlerMapping = handlerMapping;
        this.uapAuthor = uapAuthor;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /// 无法完整迁移原有的拦截器功能，具体方法的 annotation 无法获取，暂时不做
//        HandlerMethod handlerMethod = (HandlerMethod) this.handlerMapping.getHandlerInternal(exchange).toProcessor().peek();
//        IgnoreSecurity ignore = handlerMethod.getMethodAnnotation(IgnoreSecurity.class);
//        if (ignore != null) {
//            return chain.filter(exchange);
//        }
        String uapResponse = this.uapAuthor.httpRequestForUAP(HttpMethod.POST, "authorization", exchange.getRequest(),
                                                              null);
        return this.uapResponseFilter(exchange, uapResponse, chain);
    }

    /**
     * 过滤、处理 UAP 平台返回的鉴权信息
     * @param exchange
     * @param uapResponse UAP 平台返回的响应数据
     * @param chain  网关过滤器调用链
     * @return
     */
    private Mono<Void> uapResponseFilter(ServerWebExchange exchange, String uapResponse, GatewayFilterChain chain) {
        if (StringUtils.isEmpty(uapResponse)) {
            log.error("The response of UAP is null, can not validate authorization info.");
            return this.authErrorResponse(exchange);
        }
        try {
            ModelResponse<AuthorizationInfo> res = JsonUtil.getModelResponse(uapResponse, AuthorizationInfo.class);
            switch (res.getMsgCode()) {
                case TokenResponseCode.SESSION_TIME_OUT:
                case TokenResponseCode.TOKEN_INVALID:
                case TokenResponseCode.TOKEN_EXPIRED:
                case TokenResponseCode.TOKEN_MISMATCH_IP:
                case TokenResponseCode.TOKEN_NOT_SET:
                    log.error("Session time out for request: " + exchange.getRequest().getPath());
                    return this.authErrorResponse(exchange);
                case TokenResponseCode.UNAUTHORIZED_ACCESS:
                    log.error("No permission for request: " + exchange.getRequest().getPath());
                    return this.authErrorResponse(exchange);
                case TokenResponseCode.OPERATE_SUCCESS:
                    if(res.getData() != null) {
                        AuthorizationInfo auth = res.getData();
                        final SessionUser sessionUser = auth.getSessionUser();
                        sessionUser.setLocal(UAPUtils.formatUAPLocal(sessionUser.getLocal()));
                        String newToken = sessionUser.getToken();
                        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                        exchange.getSession().map(webSession -> webSession.getAttributes().put("",sessionUser));
                        if(exchange.getRequest().getHeaders().getFirst(
                                RequestHeaderConstants.JWT_TOKEN_KEY).equals(newToken)) {
                            log.debug("Refresh token from UAP.");
                            exchange.getResponse().getHeaders().add(ACCESS_CONTROL_EXPOSE_HEADERS, "uap_user");
                            exchange.getResponse().getHeaders().add("uap_user", "uap_user");
                        }
                    }
                    return chain.filter(exchange);
                default:
                    log.error("Unexpected response code from uap.");
                    return this.authErrorResponse(exchange);
            }
        } catch (Exception e) {
            log.error("The response of UAP could not be format as AuthorizationInfo.");
            e.printStackTrace();
            return this.authErrorResponse(exchange);
        }

    }

    /**
     * 定义鉴权异常的请求响应
     *
     * @param exchange
     * @return
     */
    private Mono<Void> authErrorResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(
                HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
