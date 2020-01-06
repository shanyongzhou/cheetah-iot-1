package com.cheetah.bootstrap.mdc.entity;

/**
 * p_sys_configuration_items 系统配置表的一个配置值
 */
public class ConfigurationProperty {
    private String key;

    private String value;

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
