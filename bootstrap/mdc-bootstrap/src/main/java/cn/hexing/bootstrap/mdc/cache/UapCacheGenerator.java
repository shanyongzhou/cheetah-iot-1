package com.cheetah.bootstrap.mdc.cache;

import com.cheetah.common.core.service.UapAppInformationCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用于处理 UAP 平台相关的缓存信息，
 */
@Component
public class UapCacheGenerator {
    private final static Logger log = LoggerFactory.getLogger(UapCacheGenerator.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public UapCacheGenerator(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void putAll(List<Map<String, Object>> uapAppMap){
        redisTemplate.opsForValue().set(UapAppInformationCache.PROPERTIES_UAP_APP, uapAppMap);
    }
}
