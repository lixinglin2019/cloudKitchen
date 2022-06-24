package com.cloudkitchens.common;

import lombok.Getter;

@Getter
public enum AppCode implements StatusCode {
    APP_ERROR(2000,"业务异常"),
    ORDER_ERROR(2002,"订单异常"),
    PRICE_ERROR(2001,"价格异常");
    private int code;
    private String message;

    AppCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMsg() {
        return null;
    }
}
