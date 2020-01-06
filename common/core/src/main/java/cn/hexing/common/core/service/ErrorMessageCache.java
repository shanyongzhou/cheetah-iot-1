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

import static com.cheetah.common.core.util.CacheNameConstants.CACHE_ERROR_MESSAGE;

/**
 * 异常、错误信息缓存管理
 *
 * @author cheetah.zsy
 */
@Service
public class ErrorMessageCache implements CacheOperator {
    private static Logger log = LoggerFactory.getLogger(ErrorMessageCache.class);
    /**
     * mdc 错误信息国际化
     */
    public static final String I18N_MDC_ERROR_MESSAGE = "I18N:MDC:ERROR:MESSAGE:{locale}";

    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ApplicationContext applicationContext;

    public ErrorMessageCache(
            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取国际化后的异常信息
     *
     * @param field  异常信息 code
     * @param locale
     * @return 国际化后的异常信息
     */
    public String getI18nMdcErrorMessage(String field, Locale locale) {
        return this.getI18nMdcErrorMessage(field, locale.toString());
    }

    /**
     * 获取国际化后的异常信息
     *
     * @param field        异常信息 code
     * @param localeString 国际化 code
     * @return 国际化后的异常信息
     */
    @Cacheable(value = CACHE_ERROR_MESSAGE)
    public String getI18nMdcErrorMessage(String field, String localeString) {
        String key = I18N_MDC_ERROR_MESSAGE.replace("{locale}", localeString);
        return String.valueOf(redisTemplate.opsForHash().get(key, field));
    }

    @CacheEvict(value = CACHE_ERROR_MESSAGE, allEntries = true)
    public void evictErrorMessageLocale() {
        log.info("Clean the cache of locale error message.");
    }

    @CacheEvict(value = CACHE_ERROR_MESSAGE)
    public void evictErrorMessageLocale(String field, String localeString) {
        log.info("Clean the cache of locale error message, field=" + field + ", locale=" + localeString);
    }

    @Override
    public void fromMessage(CacheMessage cacheMessage) {
        if (!CACHE_ERROR_MESSAGE.equals(cacheMessage.getName())) {
            return;
        }
        // 动态代理的折中办法，取得代理bean，再使用代理bean的方法，使切面生效
        ErrorMessageCache errorMessageCache = applicationContext.getBean(ErrorMessageCache.class);
        switch (cacheMessage.getScope()) {
            case ALL:
                this.operationForAll(errorMessageCache, cacheMessage.getCacheOperation());
                break;
            case ONE:
                this.operationForOne(errorMessageCache, cacheMessage.getCacheOperation(), cacheMessage.getLocale(),
                                     cacheMessage.getKey());
                break;
            default:
                log.info("Undefined scope for error message cache, nothing happened.");
        }
    }

    private void operationForOne(ErrorMessageCache errorMessageCache, CacheOperation cacheOperation, Locale locale,
                                 String key) {
        switch (cacheOperation) {
            case EVICT:
                errorMessageCache.evictErrorMessageLocale(locale.toString(), key);
                break;
            case UPDATE:
                errorMessageCache.evictErrorMessageLocale(locale.toString(), key);
                errorMessageCache.getI18nMdcErrorMessage(locale.toString(), key);
                break;
            default:
                log.info("Undefined operation for error message cache, nothing happened.");
        }
    }

    private void operationForAll(ErrorMessageCache errorMessageCache, CacheOperation cacheOperation) {
        switch (cacheOperation) {
            case UPDATE:
            case EVICT:
                errorMessageCache.evictErrorMessageLocale();
                break;
            default:
                log.info("Undefined operation for error message cache, nothing happened.");
        }
    }
}
