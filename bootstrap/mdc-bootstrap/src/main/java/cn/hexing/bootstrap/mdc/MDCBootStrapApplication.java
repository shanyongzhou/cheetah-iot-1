package com.cheetah.bootstrap.mdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cheetah.**"})
public class MDCBootStrapApplication {
    public static void main(String[] args) {
        SpringApplication.run(MDCBootStrapApplication.class, args);
    }
}
