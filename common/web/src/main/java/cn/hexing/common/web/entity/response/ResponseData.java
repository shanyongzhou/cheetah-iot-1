package com.cheetah.common.web.entity.response;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 响应内容实体
 */
public class ResponseData {
    /**
     * 成功默认响应code
     */
    public static final String OK_CODE = "0";
    /**
     * 成功的默认响应实例
     */
    public static ResponseData OK = new ResponseData(OK_CODE, "success");
    /**
     * 响应code，值与异常代码有关
     */
    private String code;
    /**
     * 响应内容，当请求失败时，响应内容即是错误信息
     */
    private Object content;

    @JsonCreator
    public ResponseData(String code, Object content) {
        this.code = code;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
