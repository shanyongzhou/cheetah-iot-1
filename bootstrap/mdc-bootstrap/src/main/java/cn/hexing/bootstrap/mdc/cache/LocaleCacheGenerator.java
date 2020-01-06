package com.cheetah.bootstrap.mdc.cache;

import com.cheetah.bootstrap.mdc.entity.LocaleMessage;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 国际化资源缓存生成器接口，负责redis缓存的新增和更新，同时负责缓存消息的推送
 *
 * @author cheetah.zsy
 */
public interface LocaleCacheGenerator {
    /**
     * 初始化所有国际化缓存
     *
     * @param localeMessagesList 国际化信息列表
     */
    void putAll(List<LocaleMessage> localeMessagesList);

    /**
     * 初始化指定语言区域的所有国际化缓存
     *
     * @param localeMap    要缓存的 hash 表
     * @param localeString
     */
    void putAll(Map<String, String> localeMap, String localeString);

    /**
     * 完全更新所有国际化缓存
     *
     * @param localeMessagesList 国际化信息列表
     */
    void updateAll(List<LocaleMessage> localeMessagesList);

    /**
     * 完全更新指定语言区域的所有国际化缓存
     *
     * @param localeMap    要缓存的 hash 表
     * @param localeString
     */
    void updateAll(Map<String, String> localeMap, String localeString);

    /**
     * 更新指定的某一个国际化缓存
     *
     * @param hashKey      名称
     * @param value        值
     * @param localeString
     */
    void updateOne(String hashKey, String value, String localeString);

    /**
     * 过滤 LocaleMessage 列表，生成对应语言地区的国际化资源的 hash 表
     *
     * @param localeMessagesList LocaleMessage 列表，通常由数据库直接查询获得
     * @param locale             语言地区
     * @return 对应语言地区的国际化资源的 hash 表
     */
    default Map<String, String> filterLocaleMessages(List<LocaleMessage> localeMessagesList, Locale locale) {
        return localeMessagesList.stream().filter(
                localeMessage -> locale.toString().equals(
                        localeMessage.getLocaleString())).distinct().filter(
                localeMessage -> !StringUtils.isEmpty(localeMessage.getText())).collect(
                Collectors.toMap(localeMessage -> localeMessage.getCode(), localeMessage -> localeMessage.getText()));
    }
}
