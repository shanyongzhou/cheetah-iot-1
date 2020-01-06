package com.cheetah.common.core.service;

import com.cheetah.common.core.entity.message.CacheMessage;

public interface CacheMessageHandler {
    /**
     * cache message 处理接口
     * @param cacheMessage
     */
    void handleCacheMessage(CacheMessage cacheMessage);
}
