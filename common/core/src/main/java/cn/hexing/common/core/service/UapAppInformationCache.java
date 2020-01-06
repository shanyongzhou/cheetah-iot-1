package com.cheetah.common.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * UAP 平台定义的 app 相关信息缓存类
 */
@Service
public class UapAppInformationCache {
    private final static Logger log = LoggerFactory.getLogger(UapAppInformationCache.class);

    /**
     * redis key
     */
    public final static String PROPERTIES_UAP_APP = "PROPERTY:UAP:APP";

    private final RedisTemplate<String, Object> redisTemplate;

    public UapAppInformationCache(
            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取指定 app 的 url
     * @param appCode uap 平台中 app 所属的唯一 code
     * @return url，若 redis 上没有，则返回 null
     */
    public String getUapAppUrl(String appCode){
        if(StringUtils.isEmpty(appCode)){
            return null;
        }
        Object appObject = redisTemplate.opsForValue().get(PROPERTIES_UAP_APP);
        if(appObject != null) {
            String uapUrl = "";
            List<Object> appList = (List<Object>) appObject;
            // app 的 code 是唯一的
            for(Object obj: appList) {
                Map<String,Object> appMap = (Map<String, Object>) obj;
                if(appCode.equals(String.valueOf(appMap.get("CODE")))) {
                    return String.valueOf(appMap.get("URL"));
                }
            }
        }
        return null;
    }
}
