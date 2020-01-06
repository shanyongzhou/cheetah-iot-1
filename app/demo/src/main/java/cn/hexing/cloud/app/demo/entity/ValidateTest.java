package com.cheetah.cloud.app.demo.entity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class ValidateTest {
    @NotNull
    private String notNull;

    @Null
    private String nullString;

    @DecimalMax("999999")
    private final int finalIntMax;

    public ValidateTest(@DecimalMax("999999") int finalIntMax) {
        this.finalIntMax = finalIntMax;
    }

    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    public int getFinalIntMax() {
        return finalIntMax;
    }
}
