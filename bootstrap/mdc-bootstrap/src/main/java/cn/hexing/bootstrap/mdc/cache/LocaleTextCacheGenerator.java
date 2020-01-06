package com.cheetah.bootstrap.mdc.cache;

import com.cheetah.bootstrap.mdc.channel.CacheChannel;
import com.cheetah.bootstrap.mdc.entity.LocaleMessage;
import com.cheetah.common.core.entity.message.CacheMessage;
import com.cheetah.common.core.entity.message.CacheOperation;
import com.cheetah.common.core.entity.message.OperateScope;
import com.cheetah.common.core.service.LocaleCache;
import com.cheetah.common.core.util.LocaleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 国际化文本缓存生成器
 *
 * @author cheetah.zsy
 */
@Service
public class LocaleTextCacheGenerator implements LocaleCacheGenerator {
    private static Logger log = LoggerFactory.getLogger(LocaleTextCacheGenerator.class);
    private final RedisTemplate<String, Object> redisTemplate;

    private final MessageChannel output;

    public LocaleTextCacheGenerator(
            RedisTemplate<String, Object> redisTemplate, @Qualifier(CacheChannel.UPDATE_OUTPUT) MessageChannel output) {
        this.redisTemplate = redisTemplate;
        this.output = output;
    }

    @Override
    public void putAll(List<LocaleMessage> localeMessagesList) {
        if (localeMessagesList == null || localeMessagesList.size() == 0) {
            log.debug("No locale text data to add.");
            return;
        }
        // 目前只有简体中文和美式英文
        this.putAll(this.filterLocaleMessages(localeMessagesList, Locale.SIMPLIFIED_CHINESE),
                    Locale.SIMPLIFIED_CHINESE.toString());
        this.putAll(this.filterLocaleMessages(localeMessagesList, Locale.US), Locale.US.toString());
        log.info("Init all the locale text cache.");
    }

    @Override
    public void putAll(Map<String, String> localeMap, String localeString) {
        if (StringUtils.isEmpty(localeMap)) {
            log.error("Locale text cache needed [localeString] as parameter, but got [" + localeString + "].");
            return;
        }
        redisTemplate.opsForHash().putAll(LocaleCache.I18N_MDC_SYSTEM.replace("{locale}", localeString),
                                          localeMap);
        log.debug("Init locale text cache with locale [" + localeString + "] success.");
    }

    @Override
    public void updateAll(List<LocaleMessage> localeMessagesList) {
        if (localeMessagesList == null || localeMessagesList.size() == 0) {
            log.debug("No locale text data to update.");
            return;
        }
        // 目前只有简体中文和美式英文
        this.updateAll(this.filterLocaleMessages(localeMessagesList, Locale.SIMPLIFIED_CHINESE),
                       Locale.SIMPLIFIED_CHINESE.toString());
        this.updateAll(this.filterLocaleMessages(localeMessagesList, Locale.US), Locale.US.toString());
        log.info("Update all the locale text cache.");
    }

    @Override
    public void updateAll(Map<String, String> localeMap, String localeString) {
        if (localeMap == null || localeMap.size() == 0) {
            log.debug("No locale text data to update.");
            return;
        }
        if (StringUtils.isEmpty(localeString)) {
            log.error("Locale text cache needed [localeString] as parameter, but got [" + localeString + "].");
            return;
        }
        String key = LocaleCache.I18N_MDC_SYSTEM.replace("{locale}", localeString);
        redisTemplate.delete(key);
        redisTemplate.opsForHash().putAll(key, localeMap);
        log.info("Update all the error message cache with locale [" + localeString + "] success.");
    }

    @Override
    public void updateOne(String hashKey, String value, String localeString) {
        if (StringUtils.isEmpty(localeString)) {
            log.error("Locale text cache needed [localeString] as parameter, but got [" + localeString + "].");
            return;
        }
        String key = LocaleCache.I18N_MDC_SYSTEM.replace("{locale}", localeString);
        redisTemplate.opsForHash().delete(key, hashKey);
        redisTemplate.opsForHash().put(key, hashKey, value);
        log.info("Update the locale text cache with key [" + hashKey + "]locale [" + localeString + "] success.");
        CacheMessage cacheMessage = new CacheMessage(CacheOperation.UPDATE, OperateScope.ONE,
                                                     LocaleCache.I18N_MDC_SYSTEM, hashKey,
                                                     LocaleStringUtils.fromLocaleString(localeString));
        output.send(MessageBuilder.withPayload(cacheMessage).build());
        log.info(
                "Send the message of updating the locale text cache with key [" + hashKey + "]locale [" + localeString + "] success.");
    }
}
