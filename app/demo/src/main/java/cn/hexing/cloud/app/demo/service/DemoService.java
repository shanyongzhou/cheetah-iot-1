package com.cheetah.cloud.app.demo.service;

import com.cheetah.cloud.app.demo.dao.DemoDao;
import com.cheetah.common.core.service.LocaleCache;
import com.cheetah.common.core.service.SystemConfigCache;
import com.cheetah.common.web.entity.system.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * 展示微服务基础功能用的demo
 *
 * @author cheetah.zsy
 */
@Service
public class DemoService {
    private final SystemConfigCache systemConfigCache;
    private final LocaleCache localeCache;
    private final DemoDao demoDao;

    public DemoService(SystemConfigCache systemConfigCache, LocaleCache localeCache,
                       DemoDao demoDao) {
        this.systemConfigCache = systemConfigCache;
        this.localeCache = localeCache;
        this.demoDao = demoDao;
    }

    /**
     * 直接从数据库查询的示例，此处只是示例，正常开发时，p_code这种读频率和数量远大于写的资源需要进行缓存，如果是公共资源，需要做redis、local两级缓存，如果是本应用才会使用的资源，只用做本地缓存。
     * 但是不论是那种缓存都需要慎重考虑过期时间和更新策略，以防缓存穿透和雪崩。
     * 请查看另一个使用缓存的示例
     *
     * @param code         code 名称
     * @param localeString 语言区域
     * @return code 对应的选项的列表，Pair.key 为code 的value， Pair.value 为国际化后的 text
     */
    public List<Pair> getOptionListDirectly(String code, Locale localeString) {
        List<Pair> optionList = demoDao.queryOptions(code, localeString.toString());
        return optionList;
    }

    /**
     * 从缓存中获取系统配置
     *
     * @param code
     * @return
     */
    public String getSystemConfig(String code) {
        return systemConfigCache.getMDCSystemConfigValue(code);
    }


}
