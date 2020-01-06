package com.cheetah.common.core.config;

import com.cheetah.common.core.validate.LocaleValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 提供几个Validator的默认实现
 */
@Configuration
public class ValidateConfig {
    @Bean
    @Primary
    public LocalValidatorFactoryBean getValidatorFactory() {
        return new LocalValidatorFactoryBean();
    }

    @Bean("USValidator")
    public Validator getUSValidator(ValidatorFactory validatorFactory) {
        return LocaleValidatorFactory.getUsValidator(validatorFactory);
    }

    @Bean("ChineseValidator")
    public Validator getChineseValidator(ValidatorFactory validatorFactory) {
        return LocaleValidatorFactory.getChineseValidator(validatorFactory);
    }

    @Bean("FrenchValidator")
    public Validator getFrenchValidator(ValidatorFactory validatorFactory) {
        return LocaleValidatorFactory.getFrenchValidator(validatorFactory);
    }

}
