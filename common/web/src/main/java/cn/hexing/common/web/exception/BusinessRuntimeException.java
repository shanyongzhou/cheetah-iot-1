package com.cheetah.common.web.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * runtime 类型的自定义异常，由于事务管理器默认回滚runtime 异常，如果不想配置指定特定的回滚异常类，需要使用此类作为基类
 * @author cheetah.zsy
 */
public class BusinessRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 异常代码，具体需参考异常代码表
     */
    private String errorCode;

    /**
     * 异常附带信息列表，具体的异常信息，与异常模板组合形成最终的异常详细信息
     */
    private List<String> messageList;

    public BusinessRuntimeException(String errorCode, String message) {
        super(errorCode);
        this.messageList = new ArrayList<>();
        this.messageList.add(message);
        this.errorCode = errorCode;
    }

    public BusinessRuntimeException(String errorCode, String ... messages) {
        super(errorCode);
        this.messageList = new ArrayList<>();
        this.messageList.addAll(Arrays.asList(messages));
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }
}
