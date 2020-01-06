package com.cheetah.bootstrap.mdc.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动应用后进行缓存初始化。目前MDCBootstrap应用的主要作用就是管理缓存。
 *
 * @author cheetah.zsy
 */
@Component
public class CacheInitRunner implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(CacheInitRunner.class);
    private final SystemConfigService systemConfigService;
    private final LocaleTextService localeTextService;
    private final ErrorMessageService errorMessageService;

    public CacheInitRunner(SystemConfigService systemConfigService,
                           LocaleTextService localeTextService,
                           ErrorMessageService errorMessageService) {
        this.systemConfigService = systemConfigService;
        this.localeTextService = localeTextService;
        this.errorMessageService = errorMessageService;
    }

    @Override
    public void run(String... args) throws Exception {
        systemConfigService.refreshSystemConifg();
        localeTextService.initLocaleTextCache();
        errorMessageService.initErrorMessageCache();
        logger.info("MDC bootstrap success.");
    }
}
