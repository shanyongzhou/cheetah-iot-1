package com.cheetah.bootstrap.mdc.service;

import com.cheetah.bootstrap.mdc.cache.LocaleTextCacheGenerator;
import com.cheetah.bootstrap.mdc.dao.LocaleDao;
import com.cheetah.bootstrap.mdc.entity.LocaleMessage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提供国际化文本资源的操作与服务，不包括错误信息的国际化文本
 *
 * @author cheetah.zsy
 */
@Service
public class LocaleTextService {
    private final LocaleDao localeDao;

    private final LocaleTextCacheGenerator localeTextCacheGenerator;

    public LocaleTextService(LocaleDao localeDao,
                             LocaleTextCacheGenerator localeTextCacheGenerator) {
        this.localeDao = localeDao;
        this.localeTextCacheGenerator = localeTextCacheGenerator;
    }

    /**
     * 初始化文本国际化缓存，新增redis缓存
     *
     * @return
     */
    public boolean initLocaleTextCache() {
        List<LocaleMessage> localeTextList = localeDao.getLocaleTextList();
        localeTextCacheGenerator.putAll(localeTextList);
        return true;
    }

    /**
     * 刷新所有文本国际化缓存，不包括错误信息缓存，更新redis缓存并向消息通道推送国际化缓存更新的消息
     *
     * @return
     */
    public boolean updateAllLocaleTextCache() {
        List<LocaleMessage> localeTextList = localeDao.getLocaleTextList();
        localeTextCacheGenerator.updateAll(localeTextList);
        return true;
    }

    /**
     * 刷新一个特定的国际化文本缓存，不包括错误信息缓存，更新redis中国际化所在的hash中一个特定key的缓存，同时向消息通道推送更新一个国际化文本缓存的消息
     *
     * @param key          文本key
     * @param localeString 语言区域
     * @return
     */
    public boolean updateOneOfLocaleTextCache(String key, String localeString) {
        LocaleMessage localeMessage = localeDao.getLocaleText(key, localeString);
        localeTextCacheGenerator.updateOne(key, localeMessage.getText(), localeString);
        return true;
    }
}
