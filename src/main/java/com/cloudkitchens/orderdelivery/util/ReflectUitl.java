package com.cloudkitchens.orderdelivery.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class ReflectUitl {

    public static Method getMethodName(JoinPoint point, MethodSignature sig) throws NoSuchMethodException {
        MethodSignature msig = sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        return currentMethod;
    }

    //该注解只能用于方法上
    public static Signature validateUseMethod(JoinPoint point) {
        Signature sig = point.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        return sig;
    }
}
