package com.cheetah.bootstrap.mdc.entity;

import java.util.Objects;

/**
 * 一段信息的国际化结构
 */
public class LocaleMessage {
    private String code;

    private String text;

    private String localeString;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocaleString() {
        return localeString;
    }

    public void setLocaleString(String localeString) {
        this.localeString = localeString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocaleMessage that = (LocaleMessage) o;
        return code.equals(that.code) &&
                localeString.equals(that.localeString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, localeString);
    }
}
