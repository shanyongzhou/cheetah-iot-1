package com.cheetah.cloud.app.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 示例应用启动类
 * @author cheetah.zsy
 */
@SpringBootApplication(scanBasePackages = {"com.cheetah.**"})
@EnableFeignClients
@EnableCaching
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
