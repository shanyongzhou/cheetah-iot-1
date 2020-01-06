package com.cheetah.bootstrap.mdc.service;

import com.cheetah.bootstrap.mdc.cache.UapCacheGenerator;
import com.cheetah.bootstrap.mdc.dao.UAPConfigDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * uap 平台配置相关业务功能
 */
@Service
public class UapConfigService {
    private final UAPConfigDao uapConfigDao;
    private final UapCacheGenerator uapCacheGenerator;

    public UapConfigService(UAPConfigDao uapConfigDao,
                            UapCacheGenerator uapCacheGenerator) {
        this.uapConfigDao = uapConfigDao;
        this.uapCacheGenerator = uapCacheGenerator;
    }

    public boolean initUapConfig(){
        List<Map<String, Object>> uapAppUrls = uapConfigDao.getUapAppUrls();
        uapCacheGenerator.putAll(uapAppUrls);
        return true;
    }
}
