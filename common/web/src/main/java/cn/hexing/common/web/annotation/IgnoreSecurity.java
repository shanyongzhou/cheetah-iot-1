package com.cheetah.common.web.annotation;

import java.lang.annotation.*;

/***
 * 标识是否忽略REST安全性检查
 * @author HEZ409
 *
 */
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface IgnoreSecurity {

}
