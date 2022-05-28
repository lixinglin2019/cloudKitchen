package com.cloudkitchens.orderdelivery.aop.annotation;


import java.lang.annotation.*;

/**
 * 标记需要做业务日志的方法
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CounterRateLimitAnnocation {

    int timeLimit() default 5;
    int numLimit() default 60;

}
