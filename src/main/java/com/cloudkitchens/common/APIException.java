package com.cloudkitchens.common;

import lombok.Data;

@Data
public class APIException extends RuntimeException {
    private int code;
    private String message;

    public APIException(StatusCode statusCode, String message) {
        super(message);
        this.code = statusCode.getCode();
        this.message =  statusCode.getMsg();
    }

    public APIException(String message) {
        super(message);
        this.code = AppCode.APP_ERROR.getCode();
        this.message = AppCode.APP_ERROR.getMessage();
    }
}
