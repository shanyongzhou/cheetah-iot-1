package com.cheetah.common.core.service;

import com.cheetah.common.core.entity.message.CacheMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.cheetah.common.core.util.CacheNameConstants.*;

/**
 * 默认的缓存消息处理器实现
 * @author cheetah.zsy
 */
@Component
public class DefaultCacheMessageHandler implements CacheMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultCacheMessageHandler.class);
    private final LocaleCache localeCache;
    private final SystemConfigCache systemConfigCache;
    private final ErrorMessageCache errorMessageCache;

    public DefaultCacheMessageHandler(LocaleCache localeCache,
                                      SystemConfigCache systemConfigCache,
                                      ErrorMessageCache errorMessageCache) {
        this.localeCache = localeCache;
        this.systemConfigCache = systemConfigCache;
        this.errorMessageCache = errorMessageCache;
    }

    @Override
    public void handleCacheMessage(CacheMessage cacheMessage) {
        CacheOperator cacheOperator = null;
        switch (cacheMessage.getName()) {
            case CACHE_TEXT_LOCALE:
                cacheOperator = localeCache;
                break;
            case CACHE_ERROR_MESSAGE:
                cacheOperator = errorMessageCache;
                break;
            case CACHE_SYSTEM_CONFIG:
                cacheOperator = systemConfigCache;
                break;
            default:
                log.error("Unknown cache message from the cheetah_cache topic, nothing changes.");
                return;
        }
        cacheOperator.fromMessage(cacheMessage);
    }
}
