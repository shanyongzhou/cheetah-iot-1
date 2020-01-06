package com.cheetah.common.web.util;

import com.cheetah.common.web.entity.response.ResponseData;

/**
 * 异常处理相关静态方法
 */
public class ExceptionUtil {
    /**
     * 服务限流默认 fallback
     *
     * @param throwable 捕获到的异常，如果不设置额外的 exceptionsToIgnore 就会捕获所有异常
     * @return
     */
    public static ResponseData defaultFallbackForRateLimit(Throwable throwable) {
        return new ResponseData("", "The rate of calls has been limited, please try again after a few seconds.");
    }
}
