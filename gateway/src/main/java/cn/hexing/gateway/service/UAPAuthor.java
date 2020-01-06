package com.cheetah.gateway.service;

import com.cheetah.common.web.util.RequestHeaderConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexing.uap.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * UAP 平台鉴权服务
 */
@Component
public class UAPAuthor {
    private static final Logger log = LoggerFactory.getLogger(UAPAuthor.class);

    @Value("${uap.url}")
    private String uapBaseServerUrl;

    /**
     * 转发请求到uap平台
     * @param httpMethod 请求 uap 平台的 HttpMethod
     * @param requestMapping 请求 uap 平台的 RequestMapping
     * @param serverHttpRequest 原请求对象
     * @param data 请求的具体数据
     * @return uap 平台返回的结果，均为 String
     */
    public String httpRequestForUAP(HttpMethod httpMethod, String requestMapping, ServerHttpRequest serverHttpRequest, Object data){
        StringBuilder uapRequestUrlBuilder = new StringBuilder(this.uapBaseServerUrl);
        if (!this.uapBaseServerUrl.endsWith("/")){
            uapRequestUrlBuilder.append("/");
        }
        uapRequestUrlBuilder.append(requestMapping);
        String jsonString = null;
        try {
            jsonString = new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Failed to transform data to JSONString");
            e.printStackTrace();
        }
        return this.httpRequestForUAP(httpMethod, uapRequestUrlBuilder.toString(), serverHttpRequest.getHeaders().getFirst(
                RequestHeaderConstants.JWT_TOKEN_KEY), jsonString);
    }

    /**
     * 通过 uap-common 包的 HttpUtil 向 UAP 平台发送请求
     * @param httpMethod 请求 method
     * @param serviceUrl 请求 url
     * @param token 存在 header 中用于 JWT 的token
     * @param jsonString 请求体（uap-common 只提供了 get、post、put方法，其中只有post和put带请求体，且均为 json 格式字符串
     * @return UAP 平台返回的结果
     */
    public String httpRequestForUAP(HttpMethod httpMethod, String serviceUrl, String token, String jsonString)  {
        try {
            switch (httpMethod) {
                case GET:
                    return HttpUtil.get(serviceUrl, token);
                case POST:
                    return HttpUtil.post(serviceUrl, token, jsonString);
                case PUT:
                    return HttpUtil.put(serviceUrl, token, jsonString);
                default:
                    log.error("Nonsupport Method = [ " + httpMethod.name() + "], url = " + serviceUrl);
                    return "";
            }
        } catch (Exception e) {
            log.error("Request failed for uap! Method = [ " + httpMethod.name() + "], url = " + serviceUrl);
            e.printStackTrace();
        }
        return "";
    }


}
