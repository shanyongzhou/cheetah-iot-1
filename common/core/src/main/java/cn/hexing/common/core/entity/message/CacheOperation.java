package com.cheetah.common.core.entity.message;

/**
 * 缓存操作枚举
 */
public enum CacheOperation {
    /**
     * 更新，强制删除本地缓存并获取新缓存
     */
    UPDATE,
    /**
     * 删除本地缓存
     */
    EVICT
}
