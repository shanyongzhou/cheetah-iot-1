package com.cheetah.bootstrap.mdc.controller;

import com.cheetah.bootstrap.mdc.service.ErrorMessageService;
import com.cheetah.common.web.entity.response.ResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MDC 国际化操作 endpoint
 *
 * @author cheetah.zsy
 */
@RestController
@RequestMapping("locale")
public class LocaleResourceController {
    private final ErrorMessageService localeService;

    public LocaleResourceController(ErrorMessageService localeService) {
        this.localeService = localeService;
    }

    /**
     * 刷新所有国际化缓存
     * @return
     */
    @PostMapping("/refresh")
    public ResponseData refreshAllLocale() {
        localeService.updateAllErrorCodeCache();
        return ResponseData.OK;
    }
}
