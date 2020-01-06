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

import java.util.Locale;

import static com.cheetah.common.core.util.CacheNameConstants.CACHE_TEXT_LOCALE;

/**
 * mdc 国际化资源缓存管理
 *
 * @author cheetah.zsy
 */
@Service
public class LocaleCache implements CacheOperator {
    private static final Logger log = LoggerFactory.getLogger(LocaleCache.class);
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * mdc 系统国际化
     */
    public static final String I18N_MDC_SYSTEM = "I18N:MDC:SYSTEM:{locale}";

    public LocaleCache(
            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取国际化后的系统文本
     *
     * @param field        文本 key
     * @param localeString
     * @return 国际化后的系统文本
     */
    @Cacheable(value = CACHE_TEXT_LOCALE)
    public String getI18nMdcSystemText(String field, String localeString) {
        String key = I18N_MDC_SYSTEM.replace("{locale}", localeString);
        return String.valueOf(redisTemplate.opsForHash().get(key, field));
    }

    /**
     * 获取国际化后的系统文本
     *
     * @param field  文本 key
     * @param locale
     * @return 国际化后的系统文本
     */
    public String getI18nMdcSystemText(String field, Locale locale) {
        return this.getI18nMdcSystemText(field, locale.toString());
    }

    @CacheEvict(value = CACHE_TEXT_LOCALE, allEntries = true)
    public void evictSystemTextLocale() {
        log.info("Clean all the cache of system locale text.");
    }

    @CacheEvict(value = CACHE_TEXT_LOCALE)
    public void evictSystemTextLocale(String field, String localeString) {
        log.info("Clean the cache of system locale text, field=" + field + ", locale=" + localeString);
    }

    @Override
    public void fromMessage(CacheMessage cacheMessage) {
        if (!CACHE_TEXT_LOCALE.equals(cacheMessage.getName())) {
            return;
        }
        // 动态代理的折中办法，取得代理bean，再使用代理bean的方法，使切面生效
        LocaleCache localeCache = applicationContext.getBean(LocaleCache.class);
        switch (cacheMessage.getScope()) {
            case ALL:
                this.operationForAll(localeCache, cacheMessage.getCacheOperation());
                break;
            case ONE:
                this.operationForOne(localeCache, cacheMessage.getCacheOperation(), cacheMessage.getLocale(),
                                     cacheMessage.getKey());
                break;
            default:
                log.info("Undefined scope for locale text cache, nothing happened.");
        }
    }

    private void operationForOne(LocaleCache localeCache, CacheOperation cacheOperation, Locale locale, String key) {
        switch (cacheOperation) {
            case EVICT:
                localeCache.evictSystemTextLocale(locale.toString(), key);
                break;
            case UPDATE:
                localeCache.evictSystemTextLocale(locale.toString(), key);
                localeCache.getI18nMdcSystemText(locale.toString(), key);
                break;
            default:
                log.info("Undefined operation for locale text cache, nothing happened.");
        }
    }

    private void operationForAll(LocaleCache localeCache, CacheOperation cacheOperation) {
        switch (cacheOperation) {
            case UPDATE:
            case EVICT:
                localeCache.evictSystemTextLocale();
                break;
            default:
                log.info("Undefined operation for locale text cache, nothing happened.");
        }
    }
}
