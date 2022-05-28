//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cloudkitchens.orderdelivery.common;

public enum ErrorCodeEum  {
    SUCCESS(0, "success"),
    FAIL(1, "fail");

    private int code;
    private String msg;

    private ErrorCodeEum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
