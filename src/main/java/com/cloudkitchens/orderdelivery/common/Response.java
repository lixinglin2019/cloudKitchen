package com.cloudkitchens.orderdelivery.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("前端响应对象")
@Data
public class Response<T> implements Serializable {


    public static ErrorCodeEum success = ErrorCodeEum.SUCCESS;

    public static ErrorCodeEum FAIL = ErrorCodeEum.FAIL;

    @ApiModelProperty("响应码")
    private int code;

    @ApiModelProperty("响应消息")
    private String msg;

    @ApiModelProperty("响应数据")
    private T data;

    public Response(final int code, final String msg, final T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> succ(String msg, T data) {
        return new Response(success.getCode(), msg, data);
    }

    public static <T> Response<T> succ(T data) {
        return succ(success.getMsg(), data);
    }

    public static <T> Response<T> fail(int code, String msg) {
        return new Response(code, msg, (Object) null);
    }

    public static <T> Response<T> fail(String msg) {
        return fail(FAIL.getCode(), msg);
    }

    public static <T> Response<T> fail() {
        return fail(FAIL.getMsg());
    }

}
