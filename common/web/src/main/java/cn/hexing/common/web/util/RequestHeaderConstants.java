package com.cheetah.common.web.util;

/**
 * 请求 header 相关常量
 * @author cheetah.zsy
 */
public class RequestHeaderConstants {
    /**
     * 忽略error code
     */
    public static final String IGNORE_ERROR_CODE_KEY = "IgnoreErrorCodeKey";

    /**
     * JWT token 在 header 中的 name
     */
    public static final String JWT_TOKEN_KEY = "x-access-token";

    /**
     * Request 的 header 中表示请求源 locale 的 key
     */
    public static final String REQUEST_LOCALE_KEY = "x-access-lang";
}
