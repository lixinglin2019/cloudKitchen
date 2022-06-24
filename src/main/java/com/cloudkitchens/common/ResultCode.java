//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cloudkitchens.common;

public enum ResultCode implements StatusCode {
    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    VALIDATE_ERROR(2, "validate error"),

    RESPONSE_PACKAGE_ERROR(3, "response package error");

    private int code;
    private String msg;

    private ResultCode(int code, String msg) {
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
