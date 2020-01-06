package com.cheetah.common.web.handler;

import com.cheetah.common.core.service.ErrorMessageCache;
import com.cheetah.common.core.util.LanguageRegionConstants;
import com.cheetah.common.web.entity.response.ResponseData;
import com.cheetah.common.web.exception.BusinessException;
import com.cheetah.common.web.exception.BusinessRuntimeException;
import com.cheetah.common.web.util.RequestHeaderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;

import java.text.MessageFormat;
import java.util.List;

/**
 * 定义所有 @Controller 类共享的异常信息处理方法
 *
 * @author cheetah.zsy
 */
@ControllerAdvice
public class ErrorMessageResolver {
    private static final Logger log = LoggerFactory.getLogger(ErrorMessageResolver.class);
    private final ErrorMessageCache errorMessageCache;

    public ErrorMessageResolver(ErrorMessageCache errorMessageCache) {
        this.errorMessageCache = errorMessageCache;
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseData handleBusinessException(BusinessException businessException, HandlerMethod handlerMethod,
                                                WebRequest webRequest) {
        String code = businessException.getErrorCode();
        String errorMessage = this.messageFormat(code, fetchRequestLocaleString(webRequest),
                                                 businessException.getMessages());
        if (StringUtils.isEmpty(errorMessage)) {
            return new ResponseData(code, "Undefined error code.");
        }
        return new ResponseData(code, errorMessage);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseData handleBusinessException(BusinessRuntimeException businessRuntimeException,
                                                HandlerMethod handlerMethod, WebRequest webRequest) {
        String code = businessRuntimeException.getErrorCode();
        String errorMessage = this.messageFormat(code, fetchRequestLocaleString(webRequest),
                                                 businessRuntimeException.getMessageList());
        if (StringUtils.isEmpty(errorMessage)) {
            return new ResponseData(code, "Undefined error code.");
        }
        return new ResponseData(code, errorMessage);
    }

    private String fetchRequestLocaleString(WebRequest webRequest) {
        String localeString = webRequest.getHeader(RequestHeaderConstants.REQUEST_LOCALE_KEY);
        // 默认情况下使用 US 作为异常信息的 locale
        return StringUtils.isEmpty(localeString) ? LanguageRegionConstants.US : localeString;
    }

    private String messageFormat(String code, String localeCode, List<String> subValues) {
        String errorMessageText = errorMessageCache.getI18nMdcErrorMessage(code, localeCode);
        if (StringUtils.isEmpty(errorMessageText)) {
            log.error("Could not fetch error code:\"" + code + "\" in locale:[" + localeCode + "] from cache.");
            return null;
        }
        return MessageFormat.format(errorMessageText, subValues.toArray());
    }
}
