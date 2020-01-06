package com.cheetah.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * 处理系统中语言区域的工具类
 *
 * @author cheetah.zsy
 */
public final class LocaleStringUtils {
    private static Logger log = LoggerFactory.getLogger(LocaleStringUtils.class);

    /**
     * 把mdc系统中常用的表示语言区域的字符串转化为 java.util.Locale 对象
     *
     * @param localeString 表示语言区域的字符串
     * @return java.util.Locale 对象，默认 Locale.getDefault()
     */
    public static Locale fromLocaleString(String localeString) {
        if (StringUtils.isEmpty(localeString)) {
            return Locale.getDefault();
        }
        switch (localeString) {
            case LanguageRegionConstants.SIMPLIFIED_CHINESE:
                return Locale.SIMPLIFIED_CHINESE;
            case LanguageRegionConstants.US:
                return Locale.US;
            case LanguageRegionConstants.FRANCE:
                return Locale.FRANCE;
            default:
                log.error("Not support locale string, default to return a Locale.getDefault()");
                return Locale.getDefault();
        }
    }
}
