package com.cheetah.common.web.annotation;

import java.lang.annotation.*;

/***
 * 返回码国不需要翻译注解
 * @author HEZ409
 *
 */
@Target({ElementType.PARAMETER,ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface IgnoreErrorCode {

}
