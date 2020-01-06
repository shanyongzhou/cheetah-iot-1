package com.cheetah.common.core.service;

import com.cheetah.common.core.entity.message.CacheMessage;

public interface CacheOperator {
    /**
     * 使用 CacheMessage进行管理
     * @param cacheMessage
     */
    void fromMessage(CacheMessage cacheMessage);

}
