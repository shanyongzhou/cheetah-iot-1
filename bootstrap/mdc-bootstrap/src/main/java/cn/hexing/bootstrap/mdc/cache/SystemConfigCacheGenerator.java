package com.cheetah.bootstrap.mdc.cache;

import com.cheetah.bootstrap.mdc.channel.CacheChannel;
import com.cheetah.common.core.entity.message.CacheMessage;
import com.cheetah.common.core.entity.message.CacheOperation;
import com.cheetah.common.core.entity.message.OperateScope;
import com.cheetah.common.core.service.SystemConfigCache;
import com.cheetah.common.core.util.CacheNameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * 用于处理系统配置缓存的service，使用redis作为一级缓存
 * @author cheetah.zsy
 */
@Component
public class SystemConfigCacheGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigCacheGenerator.class);

    private final MessageChannel output;

    private final RedisTemplate<String, Object> redisTemplate;

    public SystemConfigCacheGenerator(@Qualifier(CacheChannel.UPDATE_OUTPUT) MessageChannel output,
                                      RedisTemplate<String, Object> redisTemplate) {
        this.output = output;
        this.redisTemplate = redisTemplate;
    }

    public void put(Map<String, String> properties) {
        redisTemplate.opsForHash().putAll(SystemConfigCache.PROPERTY_MDC_SYSTEM, properties);
    }

    /**
     * 刷新所有系统配置的缓存，并向CacheChannel发送缓存更新的消息，通知consumer
     *
     * @param properties 从数据源中取到的最新的系统配置数据集
     */
    public void updateAll(Map<String, String> properties) {
        redisTemplate.delete(SystemConfigCache.PROPERTY_MDC_SYSTEM);
        redisTemplate.opsForHash().putAll(SystemConfigCache.PROPERTY_MDC_SYSTEM, properties);
        logger.info("Refresh all the mdc system configurations.");
        CacheMessage cacheMessage = new CacheMessage(CacheOperation.UPDATE, OperateScope.ALL,
                                                     CacheNameConstants.CACHE_SYSTEM_CONFIG, "",
                                                     Locale.getDefault());
        output.send(MessageBuilder.withPayload(cacheMessage).build());
        logger.info("Send message to refresh all the cache of the mdc system configurations.");
    }

    /**
     * 刷新指定的某一个系统配置的缓存，并向 CacheChannel 发送更新消息，通知 consumer
     * @param field 配置项名称
     * @param value 配置值
     */
    public void update(String field, String value) {
        redisTemplate.opsForHash().put(SystemConfigCache.PROPERTY_MDC_SYSTEM, field, value);
        logger.info("Refresh the mdc system configurations, key=" + field);
        CacheMessage cacheMessage = new CacheMessage(CacheOperation.UPDATE, OperateScope.ONE,
                                                     CacheNameConstants.CACHE_SYSTEM_CONFIG, field,
                                                     Locale.getDefault());
        output.send(MessageBuilder.withPayload(cacheMessage).build());
        logger.info("Send message to refresh one of the cache of the mdc system configurations, key=" + field);
    }
}
