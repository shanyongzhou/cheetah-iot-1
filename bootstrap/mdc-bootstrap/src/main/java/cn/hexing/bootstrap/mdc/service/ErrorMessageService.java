package com.cheetah.bootstrap.mdc.service;

import com.cheetah.bootstrap.mdc.cache.ErrorMessageCacheGenerator;
import com.cheetah.bootstrap.mdc.dao.LocaleDao;
import com.cheetah.bootstrap.mdc.entity.LocaleMessage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 错误、异常信息相关功能 service
 * 负责生成和更新异常信息
 * @author cheetah.zsy
 */
@Service
public class ErrorMessageService {
    private final LocaleDao localeDao;

    private final ErrorMessageCacheGenerator errorCodeLocaleCache;

    public ErrorMessageService(LocaleDao localeDao, ErrorMessageCacheGenerator errorCodeLocaleCache) {
        this.localeDao = localeDao;
        this.errorCodeLocaleCache = errorCodeLocaleCache;
    }

    /**
     * 初始错误信息缓存
     *
     * @return
     */
    public boolean initErrorMessageCache() {
        List<LocaleMessage> errorCodeLocaleMessageList = localeDao.getErrorCodeLocaleMessageList();
        errorCodeLocaleCache.putAll(errorCodeLocaleMessageList);
        return true;
    }

    /**
     * 刷新所有错误信息缓存
     *
     * @return
     */
    public boolean updateAllErrorCodeCache() {
        List<LocaleMessage> errorCodeLocaleMessageList = localeDao.getErrorCodeLocaleMessageList();
        errorCodeLocaleCache.updateAll(errorCodeLocaleMessageList);
        return true;
    }

    /**
     * 刷新特定的某一个错误信息缓存
     *
     * @param key          错误信息key
     * @param localeString 语言区域
     * @return
     */
    public boolean updateOneOfErrorCodeCache(String key, String localeString) {
        LocaleMessage localeMessage = localeDao.getLocaleErrorCode(key, localeString);
        errorCodeLocaleCache.updateOne(key, localeMessage.getText(), localeString);
        return true;
    }
}
