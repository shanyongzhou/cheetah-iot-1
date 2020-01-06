package com.cheetah.common.core.service;

import com.cheetah.common.core.entity.message.CacheMessage;
import com.cheetah.common.core.entity.message.CacheOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.cheetah.common.core.util.CacheNameConstants.CACHE_SYSTEM_CONFIG;

/**
 * mdc 系统配置缓存相关操作
 *
 * @author cheetah.zsy
 */
@Service
public class SystemConfigCache implements CacheOperator {
    private static final Logger log = LoggerFactory.getLogger(SystemConfigCache.class);
    /**
     * mdc 系统配置参数
     */
    public static final String PROPERTY_MDC_SYSTEM = "PROPERTY:MDC:SYSTEM";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取 MDC 系统配置参数
     *
     * @param key
     * @return
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Cacheable(value = CACHE_SYSTEM_CONFIG)
    public String getMDCSystemConfigValue(String key) {
        return this.getHashMapValue(PROPERTY_MDC_SYSTEM, key);
    }

    /**
     * 删除所有"MDCSystemConfig"下的缓存
     */
    @CacheEvict(value = CACHE_SYSTEM_CONFIG, allEntries = true)
    public void evictSystemConfig() {
        log.info("Clean all the cache of system config.");
    }

    /**
     * 删除"MDCSystemConfig"下对应 key 的缓存
     *
     * @param key
     */
    @CacheEvict(value = CACHE_SYSTEM_CONFIG)
    public void evictSystemConfig(String key) {
        log.info("Clean the cache of system config, key:" + key);
    }

    /**
     * 根据属性键获得属性值
     *
     * @param key
     * @param field
     * @return
     */
    private String getHashMapValue(String key, String field) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Object value = entries.get(field);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override
    public void fromMessage(CacheMessage cacheMessage) {
        if (!CACHE_SYSTEM_CONFIG.equals(cacheMessage.getName())) {
            return;
        }
        // 动态代理的折中办法，取得代理bean，再使用代理bean的方法，使切面生效
        SystemConfigCache systemConfigCache = applicationContext.getBean(SystemConfigCache.class);
        switch (cacheMessage.getScope()) {
            case ALL:
                this.operationForAll(systemConfigCache, cacheMessage.getCacheOperation());
                break;
            case ONE:
                this.operationForOne(systemConfigCache, cacheMessage.getCacheOperation(), cacheMessage.getKey());
                break;
            default:
                log.info("Undefined scope for system config cache, nothing happened.");
        }
    }

    private void operationForAll(SystemConfigCache systemConfigCache, CacheOperation cacheOperation) {
        switch (cacheOperation) {
            case UPDATE:
            case EVICT:
                systemConfigCache.evictSystemConfig();
                break;
            default:
                log.info("Undefined operation for system config cache, nothing happened.");
        }
    }

    private void operationForOne(SystemConfigCache systemConfigCache, CacheOperation cacheOperation, String key) {
        switch (cacheOperation) {
            case EVICT:
                systemConfigCache.evictSystemConfig(key);
                break;
            case UPDATE:
                systemConfigCache.evictSystemConfig(key);
                systemConfigCache.getMDCSystemConfigValue(key);
                break;
            default:
                log.info("Undefined operation for system config cache, nothing happened.");
        }
    }
}
