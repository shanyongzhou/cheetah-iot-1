package com.cheetah.common.core.entity.message;

import java.util.Locale;

/**
 * 缓存消息
 */
public class CacheMessage {
    private final CacheOperation cacheOperation;

    private final OperateScope scope;

    private final String name;

    private final String key;

    private final Locale locale;

    public CacheMessage(CacheOperation cacheOperation, OperateScope scope, String name, String key,
                        Locale locale) {
        this.cacheOperation = cacheOperation;
        this.scope = scope;
        this.name = name;
        this.key = key;
        this.locale = locale;
    }

    public CacheOperation getCacheOperation() {
        return cacheOperation;
    }

    public OperateScope getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Locale getLocale() {
        return locale;
    }
}
