package com.cheetah.bootstrap.mdc.cache;

import com.cheetah.bootstrap.mdc.channel.CacheChannel;
import com.cheetah.bootstrap.mdc.entity.LocaleMessage;
import com.cheetah.common.core.entity.message.CacheMessage;
import com.cheetah.common.core.entity.message.CacheOperation;
import com.cheetah.common.core.entity.message.OperateScope;
import com.cheetah.common.core.service.ErrorMessageCache;
import com.cheetah.common.core.util.CacheNameConstants;
import com.cheetah.common.core.util.LocaleStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 异常信息的国际化资源缓存生成器
 *
 * @author cheetah.zsy
 */
@Component
public class ErrorMessageCacheGenerator implements LocaleCacheGenerator {
    private static final Logger log = LoggerFactory.getLogger(ErrorMessageCacheGenerator.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final MessageChannel output;

    public ErrorMessageCacheGenerator(
            RedisTemplate<String, Object> redisTemplate, @Qualifier(CacheChannel.UPDATE_OUTPUT) MessageChannel output) {
        this.redisTemplate = redisTemplate;
        this.output = output;
    }

    @Override
    public void putAll(List<LocaleMessage> localeMessagesList) {
        if (localeMessagesList == null || localeMessagesList.size() == 0) {
            log.debug("No error message locale data.");
            return;
        }
        // 目前只有简体中文和美式英文
        this.putAll(this.filterLocaleMessages(localeMessagesList, Locale.SIMPLIFIED_CHINESE),
                    Locale.SIMPLIFIED_CHINESE.toString());
        this.putAll(this.filterLocaleMessages(localeMessagesList, Locale.US), Locale.US.toString());
        log.info("Init all the error message cache.");
    }

    @Override
    public void putAll(Map<String, String> localeMap, String localeString) {
        if (StringUtils.isEmpty(localeMap)) {
            log.error("Locale error message cache needed [localeString] as parameter, but got [" + localeString + "].");
            return;
        }
        redisTemplate.opsForHash().putAll(ErrorMessageCache.I18N_MDC_ERROR_MESSAGE.replace("{locale}", localeString),
                                          localeMap);
        log.debug("Init error message cache with locale [" + localeString + "] success.");
    }

    @Override
    public void updateAll(List<LocaleMessage> localeMessagesList) {
        if (localeMessagesList == null || localeMessagesList.size() == 0) {
            log.debug("No error message locale data.");
            return;
        }
        // 目前只有简体中文和美式英文
        this.updateAll(this.filterLocaleMessages(localeMessagesList, Locale.SIMPLIFIED_CHINESE),
                       Locale.SIMPLIFIED_CHINESE.toString());
        this.updateAll(this.filterLocaleMessages(localeMessagesList, Locale.US), Locale.US.toString());
        log.info("Update all the error message cache.");
    }

    @Override
    public void updateAll(Map<String, String> localeMap, String localeString) {
        if (localeMap == null || localeMap.size() == 0) {
            log.debug("No error message locale data.");
            return;
        }
        if (StringUtils.isEmpty(localeMap)) {
            log.error("Locale error message cache needed [localeString] as parameter, but got [" + localeString + "].");
            return;
        }
        String key = ErrorMessageCache.I18N_MDC_ERROR_MESSAGE.replace("{locale}", localeString);
        redisTemplate.delete(key);
        redisTemplate.opsForHash().putAll(key, localeMap);
        log.info("Update all the error message cache with locale [" + localeString + "] success.");
        CacheMessage cacheMessage = new CacheMessage(CacheOperation.UPDATE, OperateScope.ALL,
                                                     CacheNameConstants.CACHE_ERROR_MESSAGE, "",
                                                     Locale.getDefault());
        output.send(MessageBuilder.withPayload(cacheMessage).build());
        log.info("Send message to refresh all the cache of the error message.");
    }

    @Override
    public void updateOne(String hashKey, String value, String localeString) {
        if (StringUtils.isEmpty(localeString)) {
            log.error("Error message cache needed [localeString] as parameter, but got [" + localeString + "].");
            return;
        }
        String key = ErrorMessageCache.I18N_MDC_ERROR_MESSAGE.replace("{locale}", localeString);
        redisTemplate.opsForHash().delete(key, hashKey);
        redisTemplate.opsForHash().put(key, hashKey, value);
        log.info("Update the error message cache with key [" + hashKey + "]locale [" + localeString + "] success.");
        CacheMessage cacheMessage = new CacheMessage(CacheOperation.UPDATE, OperateScope.ONE,
                                                     ErrorMessageCache.I18N_MDC_ERROR_MESSAGE, hashKey,
                                                     LocaleStringUtils.fromLocaleString(localeString));
        output.send(MessageBuilder.withPayload(cacheMessage).build());
        log.info(
                "Send the message of updating the error message cache with key [" + hashKey + "]locale [" + localeString + "] success.");
    }
}
