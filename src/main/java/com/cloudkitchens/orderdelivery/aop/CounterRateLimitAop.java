package com.cloudkitchens.orderdelivery.aop;

import com.cloudkitchens.orderdelivery.aop.annotation.CounterRateLimitAnnocation;
import com.cloudkitchens.orderdelivery.common.Response;
import com.cloudkitchens.orderdelivery.enums.RateLimitTypeEnum;
import com.cloudkitchens.orderdelivery.strategy.ratelimit.IRateLimit;
import com.cloudkitchens.orderdelivery.util.ReflectUitl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志打印
 */
@Aspect
@Component
public class CounterRateLimitAop {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    Map<String, IRateLimit> rateLimitStrategy = new HashMap<String, IRateLimit>();

    public CounterRateLimitAop(List<IRateLimit> rateLimits) {
        for (IRateLimit rateLimit : rateLimits) {
            String type = rateLimit.getType();
            rateLimitStrategy.put(type, rateLimit);
        }
    }

    @Pointcut(value = "@annotation(com.cloudkitchens.orderdelivery.aop.annotation.CounterRateLimitAnnocation)")
    public void cutService() {
    }


    @Around("cutService()")
    public Object counterRateLimit(ProceedingJoinPoint point) throws Throwable {
        try {
            Boolean pass = rateLimit(point);
            if (pass) {
                //通过则直接执行
                return point.proceed();
            }else {
                //被限流
                return Response.fail("被限流");
            }
        } catch (Exception e) {
            log.error("限流出错!", e);
            return Response.fail("限流时发生了异常");
        }
    }


    private Boolean rateLimit(JoinPoint point) throws Exception {

        // 校验注解用户方法
        Signature sig = ReflectUitl.validateUseMethod(point);

        //获取被注解的方法对象
        Method currentMethod = ReflectUitl.getMethodName(point, (MethodSignature) sig);
        String methodName = currentMethod.getName();

        //获取注解对象本身
        CounterRateLimitAnnocation limit = currentMethod.getAnnotation(CounterRateLimitAnnocation.class);

        int numLimit = limit.numLimit();
        int timeLimit = limit.timeLimit();
        Map<String,Object> params = new HashMap<>();
        params.put("apiName",methodName);
        params.put("numLimit",numLimit);
        params.put("timeLimit",timeLimit);
        //策略模式去搞
        Boolean pass = rateLimitStrategy.get(RateLimitTypeEnum.COUNTER).rateLimit(params);
        return pass;

    }


}
