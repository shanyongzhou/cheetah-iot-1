package com.cheetah.bootstrap.mdc.controller;

import com.cheetah.bootstrap.mdc.service.SystemConfigService;
import com.cheetah.common.web.entity.response.ResponseData;
import com.cheetah.common.web.exception.BusinessException;
import com.cheetah.common.web.exception.BusinessRuntimeException;
import com.cheetah.common.web.util.ExceptionUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * MDC 系统配置操作 endpoint
 */
@RestController
@RequestMapping("config")
public class SystemConfigController {
    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 刷新最新的所有系统配置到 redis 缓存
     *
     * @return
     */
    @PostMapping("/refresh")
    @SentinelResource(value = "update_system_config", defaultFallback = "defaultFallbackForRateLimit", blockHandlerClass = ExceptionUtil.class, exceptionsToIgnore = {
            BusinessRuntimeException.class,
            BusinessException.class})
    public ResponseData refreshConfiguration() {
        systemConfigService.refreshSystemConifg();
        return ResponseData.OK;
    }

    /**
     * 直接从数据库获取最新的系统配置 map
     *
     * @return 所有系统配置项组成的 HashMap
     */
    @GetMapping("/configmap")
    public ResponseData getConfigurationsDirectly() {
        Map<String, String> configurationList = systemConfigService.getConfigurationsDirectly();
        return new ResponseData(ResponseData.OK_CODE, configurationList);
    }

    /**
     * 直接总数据库获取最新的某一个系统配置项
     *
     * @param configurationName 请求的系统配置项名称
     * @return 对应的系统配置项的值
     */
    @GetMapping("/configvalue")
    public ResponseData getSpecificConfiguration(String configurationName) {
        String configValue = systemConfigService.getConfigurationValueByNameDirectly(configurationName);
        return new ResponseData(ResponseData.OK_CODE, configValue);
    }
}
