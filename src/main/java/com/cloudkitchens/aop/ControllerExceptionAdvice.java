package com.cloudkitchens.aop;

import com.cloudkitchens.aop.annotation.NotControllerResponseAdvice;
import com.cloudkitchens.common.APIException;
import com.cloudkitchens.common.ResultCode;
import com.cloudkitchens.common.ResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
/**
 * Spring 中 @RestControllerAdvice 注解
 * 可以拦截+获取带有 @Controller 或 @RestController 注解类的异常，--
 *
 * ---所以   RestControllerAdvice注解 ---必须配合  RestController注解，一起使用
 * 通过 @ExceptionHandler 注解设置捕获异常类型。
 */
@RestControllerAdvice(basePackages = "com.cloudkitchens")
public class ControllerExceptionAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        boolean assignableFrom = returnType.getParameterType().isAssignableFrom(ResultVO.class);

        boolean hasMethodAnnotation = returnType.hasMethodAnnotation(NotControllerResponseAdvice.class);
        boolean b = assignableFrom || hasMethodAnnotation;
        return !b;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResultVO value = new ResultVO(body);
        boolean isStringResult = returnType.getParameterType().equals(String.class);
        if (isStringResult) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String s = objectMapper.writeValueAsString(value);
                return s;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new APIException(ResultCode.RESPONSE_PACKAGE_ERROR, e.getMessage());
            }
        }
        return value;
    }

    /**
     * 通过 @ExceptionHandler 注解设置捕获异常类型。
     * @param e
     * @return
     */
    @ExceptionHandler({BindException.class})
    public ResultVO methodArgumentNotValidExceptionHandler(BindException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResultVO(ResultCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

    /**
     * 通过 @ExceptionHandler 注解设置捕获异常类型。
     * @param e
     * @return
     */
    @ExceptionHandler(APIException.class)
    public ResultVO APIExceptionHandler(APIException apiException) {
        int code = apiException.getCode();
        String message = apiException.getMessage();
        return new ResultVO(code, message, null);
    }
}
