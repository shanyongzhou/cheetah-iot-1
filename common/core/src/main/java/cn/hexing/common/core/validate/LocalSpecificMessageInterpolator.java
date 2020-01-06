/**
 *
 * @(#) LocalSpecificMessageInterpolator.java
 * @Package com.cheetah.vending.vendingtools.validate
 * 
 * Copyright © Hexing Corporation. All rights reserved.
 *
 */

package com.cheetah.common.core.validate;

import javax.validation.MessageInterpolator;
import java.util.Locale;

/**
 *  仅定制国际化的message interpolator，其他配置与传入的MessageInterpolator相同
 * 
 *  @author  cheetah.zsy
 *  @version  $Id: Exp$ 
 *
 *  2019年7月17日 下午4:08:58   cheetah.zsy   Created.
 *           
 */
public class LocalSpecificMessageInterpolator implements MessageInterpolator {
	private final MessageInterpolator defaultMessageInterpolator;
	private final Locale locale;
	public LocalSpecificMessageInterpolator(MessageInterpolator messageInterpolator, Locale locale) {
		 this.defaultMessageInterpolator = messageInterpolator;
		 this.locale = locale;
	}

	@Override
	public String interpolate(String messageTemplate, Context context) {
		return this.defaultMessageInterpolator.interpolate(messageTemplate, context, locale);
	}

	@Override
	public String interpolate(String messageTemplate, Context context, Locale locale) {
		return this.defaultMessageInterpolator.interpolate(messageTemplate, context, locale);
	}

}
