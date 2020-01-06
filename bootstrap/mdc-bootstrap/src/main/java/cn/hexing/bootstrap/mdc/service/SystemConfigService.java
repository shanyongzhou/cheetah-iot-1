package com.cheetah.bootstrap.mdc.service;

import com.cheetah.bootstrap.mdc.cache.SystemConfigCacheGenerator;
import com.cheetah.bootstrap.mdc.dao.SystemConfigDao;
import com.cheetah.bootstrap.mdc.entity.ConfigurationProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MDC 系统配置相关功能 service
 */
@Service
public class SystemConfigService {
    private final SystemConfigDao systemConfigDao;

    private final SystemConfigCacheGenerator systemConfigCacheGenerator;

    public SystemConfigService(SystemConfigDao systemConfigDao,
                               SystemConfigCacheGenerator systemConfigCacheGenerator) {
        this.systemConfigDao = systemConfigDao;
        this.systemConfigCacheGenerator = systemConfigCacheGenerator;
    }

    /**
     * 刷新当前一级缓存中的系统配置
     * @return
     */
    public boolean refreshSystemConifg(){
        Map<String, String> configurationsDirectly = this.getConfigurationsDirectly();
        systemConfigCacheGenerator.updateAll(configurationsDirectly);
        return true;
    }

    /**
     * 直接从数据库读取所有系统配置
     * @return 当前数据库中所有系统配置的 hashmap 集合，key为配置名称，value为配置值
     */
    public Map<String, String> getConfigurationsDirectly(){
        List<ConfigurationProperty> configList = systemConfigDao.getPropertiesList();
        if(configList == null || configList.size() == 0){
            return null;
        }
        // 系统配置数量不定，map容量无法确定
        Map<String, String> configMap = new HashMap<>();
        for(ConfigurationProperty configuration : configList){
            configMap.put(configuration.getKey(), configuration.getValue());
        }
        return configMap;
    }

    /**
     * 直接从数据库读取特定配置项的值
     * @param configurationName 要读取的配置项名称
     * @return 配置项值
     */
    public String getConfigurationValueByNameDirectly(String configurationName){
        return systemConfigDao.getPropertyValueByName(configurationName);
    }
}
