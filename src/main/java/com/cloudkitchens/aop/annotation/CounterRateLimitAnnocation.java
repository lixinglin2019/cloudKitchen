package com.cloudkitchens.aop.annotation;


import java.lang.annotation.*;

/**
 * 标记需要做业务日志的方法
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CounterRateLimitAnnocation {

    int timeLimit() default 5;//单位（分钟）
    int numLimit() default 60;//单位（次数）

}
