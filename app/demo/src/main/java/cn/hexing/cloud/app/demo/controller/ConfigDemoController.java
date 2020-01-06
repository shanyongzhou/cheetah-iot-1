package com.cheetah.cloud.app.demo.controller;

import com.cheetah.cloud.app.demo.entity.ValidateTest;
import com.cheetah.cloud.app.demo.feign.SystemConfigService;
import com.cheetah.cloud.app.demo.service.DemoService;
import com.cheetah.common.web.entity.response.ResponseData;
import com.cheetah.common.web.entity.system.Pair;
import com.cheetah.common.web.exception.BusinessException;
import com.cheetah.common.web.exception.BusinessRuntimeException;
import com.cheetah.common.web.util.ExceptionUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * 测试微服务相关功能的controller
 *
 * @author cheetah.zsy
 */
@RestController
@RequestMapping("/configdemo")
public class ConfigDemoController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigDemoController.class);
    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private DemoService demoService;

    @Autowired
    private HttpSession httpSession;

    @PostMapping("/refresh")
    @SentinelResource(value = "refresh_test", defaultFallback = "defaultFallbackForRateLimit", blockHandlerClass = ExceptionUtil.class, exceptionsToIgnore = {
            BusinessRuntimeException.class,
            BusinessException.class})
    public ResponseData refresh() {
        systemConfigService.refreshConfiguration();
        logger.info("Post mdcbootstrap to refresh");
        return ResponseData.OK;
    }

    @GetMapping("/value")
    public ResponseData getPropertyValue(String key){
        String value = demoService.getSystemConfig(key);
        return new ResponseData(ResponseData.OK_CODE, value);
    }

    @PostMapping("/validate")
    public ResponseData validateTest(@Valid ValidateTest validateTest){
        return ResponseData.OK;
    }

    @GetMapping("/exception")
    @SentinelResource(value = "exception_test", defaultFallback = "defaultFallbackForRateLimit", blockHandlerClass = ExceptionUtil.class, exceptionsToIgnore = {
            BusinessRuntimeException.class,
            BusinessException.class})
    public ResponseData exceptionTest() throws BusinessException {
        throw new BusinessException("VD.01010007");
    }

    @GetMapping("/options")
    @SentinelResource(value = "options", defaultFallback = "defaultFallbackForRateLimit", blockHandlerClass = ExceptionUtil.class, exceptionsToIgnore = {
            BusinessRuntimeException.class,
            BusinessException.class})
    public ResponseData getOptions(String code) {
        List<Pair> options = demoService.getOptionListDirectly(code, Locale.US);
        return new ResponseData(ResponseData.OK_CODE, options);
    }
}
