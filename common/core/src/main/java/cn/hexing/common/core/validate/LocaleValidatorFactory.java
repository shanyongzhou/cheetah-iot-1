/**
 * @(#) VendingValidatorFactory.java
 * @Package com.cheetah.vending.vendingtools.validate
 * <p>
 * Copyright © Hexing Corporation. All rights reserved.
 */

package com.cheetah.common.core.validate;

import com.cheetah.common.core.util.LanguageRegionConstants;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

import static java.util.Locale.CHINESE;
import static java.util.Locale.FRENCH;

/**
 * 用于构建vending自定义格式的bean校验器
 *
 * @author cheetah.zsy
 * @version $Id: Exp$
 *
 *          2019年7月17日 下午4:21:25 cheetah.zsy Created.
 *
 */
public final class LocaleValidatorFactory {
    private static volatile LocalSpecificMessageInterpolator chineseMessageInterpolator;
    private static volatile LocalSpecificMessageInterpolator usMessageInterpolator;
    private static volatile LocalSpecificMessageInterpolator frenchMessageInterpolator;

    /**
     * 根据用户语言选取校验器
     * @param validatorFactory 系统使用的校验器工厂
     * @param locale 用户语言
     * @return
     */
    public static Validator getLocaleValidator(ValidatorFactory validatorFactory, Locale locale) {
        return getLocaleValidator(validatorFactory, locale.toString());
    }

    /**
     * 根据用户语言选取校验器
     *
     * Author：        cheetah.zsy
     *
     * @param validatorFactory 系统使用的校验器工厂
     * @param localeString 用户语言
     * @return
     *
     */
    public static Validator getLocaleValidator(ValidatorFactory validatorFactory, String localeString) {
        if (localeString == null) {
            return getUsValidator(validatorFactory);
        }
        switch (localeString) {
            case LanguageRegionConstants
                    .SIMPLIFIED_CHINESE:
                return getChineseValidator(validatorFactory);
            case LanguageRegionConstants.FRANCE:
                return getFrenchValidator(validatorFactory);
            case LanguageRegionConstants.US:
            default:
                return getUsValidator(validatorFactory);
        }
    }

    /**
     * 获取中文校验器
     *
     * Author：        cheetah.zsy
     *
     * @param validatorFactory 系统使用的校验器工厂
     * @return
     *
     */
    public static Validator getChineseValidator(ValidatorFactory validatorFactory) {
        if (chineseMessageInterpolator == null && validatorFactory != null) {
            synchronized (LocaleValidatorFactory.class) {
                if (chineseMessageInterpolator == null) {
                    chineseMessageInterpolator = new LocalSpecificMessageInterpolator(
                            validatorFactory.getMessageInterpolator(), CHINESE);
                }
            }
        }
        return validatorFactory.usingContext().messageInterpolator(chineseMessageInterpolator).getValidator();
    }

    /**
     * 获取法语校验器
     *
     * Author：        cheetah.zsy
     *
     * @param validatorFactory 系统使用的校验器工厂
     * @return
     *
     */
    public static Validator getFrenchValidator(ValidatorFactory validatorFactory) {
        if (frenchMessageInterpolator == null && validatorFactory != null) {
            synchronized (LocaleValidatorFactory.class) {
                if (frenchMessageInterpolator == null) {
                    frenchMessageInterpolator = new LocalSpecificMessageInterpolator(
                            validatorFactory.getMessageInterpolator(), FRENCH);
                }
            }
        }
        return validatorFactory.usingContext().messageInterpolator(frenchMessageInterpolator).getValidator();
    }

    /**
     * 获取美式英语校验器
     *
     * Author：        cheetah.zsy
     *
     * @param validatorFactory 系统使用的校验器工厂
     * @return
     *
     */
    public static Validator getUsValidator(ValidatorFactory validatorFactory) {
        if (usMessageInterpolator == null && validatorFactory != null) {
            synchronized (LocaleValidatorFactory.class) {
                if (usMessageInterpolator == null) {
                    usMessageInterpolator = new LocalSpecificMessageInterpolator(
                            validatorFactory.getMessageInterpolator(), Locale.US);
                }
            }
        }
        return validatorFactory.usingContext().messageInterpolator(usMessageInterpolator).getValidator();
    }
}
