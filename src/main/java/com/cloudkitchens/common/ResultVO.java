package com.cloudkitchens.common;

import lombok.Data;

@Data
public class ResultVO {
    private int code;
    private String msg;
    private Object data;



    public ResultVO(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //默认成功的
    public ResultVO(Object data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMsg();
        this.data = data;
    }
    //根据状态枚举的
    public ResultVO(StatusCode statusCode,Object data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }
}
