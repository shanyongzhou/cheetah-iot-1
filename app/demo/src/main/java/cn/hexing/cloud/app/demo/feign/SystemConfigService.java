package com.cheetah.cloud.app.demo.feign;

import com.cheetah.common.web.entity.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("mdcbootstrap")
public interface SystemConfigService {
    @PostMapping("config/refresh")
    ResponseData refreshConfiguration();
}
