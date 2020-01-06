package com.cheetah.bootstrap.mdc.dao;

import java.util.List;
import java.util.Map;

/**
 * UAP 平台相关配置的数据库操作
 * 包括：uap_app
 *      uap_rest_api
 * @author cheetah.zsy
 */
public interface UAPConfigDao {
    /**
     * 获取当前数据库表 uap_app 中所有app 的 id，code，name 及 url信息，用于构建 app 相关缓存
     * @return
     */
    List<Map<String, Object>> getUapAppUrls();
}
