package com.cheetah.bootstrap.mdc.dao;

import com.cheetah.bootstrap.mdc.entity.LocaleMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MDC 系统相关国际化操作
 *
 * @author cheetah.zsy
 */
public interface LocaleDao {
    /**
     * 获取异常信息的国际化信息列表
     *
     * @return
     */
    List<LocaleMessage> getErrorCodeLocaleMessageList();

    /**
     * 获取文本国际化列表
     *
     * @return
     */
    List<LocaleMessage> getLocaleTextList();

    /**
     * 获取一个特定的国际化的错误信息
     *
     * @param key          错误信息key
     * @param localeString 语言区域
     * @return
     */
    LocaleMessage getLocaleErrorCode(@Param("key") String key, @Param("localeString") String localeString);

    /**
     * 获取一个特定的国际化文本
     *
     * @param key          文本key
     * @param localeString 文本语言
     * @return
     */
    LocaleMessage getLocaleText(@Param("key") String key, @Param("localeString") String localeString);
}
