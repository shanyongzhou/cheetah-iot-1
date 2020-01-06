package com.cheetah.common.web.entity.system;

/**
 * 系统中描述一个键值对的实体
 *
 * @author cheetah.zsy
 */
public class Pair {
    private String key;
    private String value;

    public Pair() {
    }

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
