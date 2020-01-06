package com.cheetah.common.web.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 普通的业务异常，在默认情况下不会触发事务回滚
 * @author cheetah.zsy
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * 异常代码，具体需参考异常代码表
     */
    private String errorCode;

    /**
     * 异常附带信息列表，具体的异常信息，与异常模板组合形成最终的异常详细信息
     */
    private List<String> messageList;

    public BusinessException(String errorCode, String message) {
        super(errorCode);
        this.messageList = new ArrayList<>();
        this.messageList.add(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String errorCode, String ... messages) {
        super(errorCode);
        this.messageList = new ArrayList<>();
        this.messageList.addAll(Arrays.asList(messages));
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public List<String> getMessages(){
        return this.messageList;
    }

}
