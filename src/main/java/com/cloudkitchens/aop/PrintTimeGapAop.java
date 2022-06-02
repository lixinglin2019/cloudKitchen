package com.cloudkitchens.aop;

import com.cloudkitchens.aop.annotation.PrintTimeGapAnnocation;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.util.ReflectUitl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志打印
 */
@Aspect
@Component
public class PrintTimeGapAop {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value = "@annotation(com.cloudkitchens.aop.annotation.PrintTimeGapAnnocation)")
    public void cutService() {
    }

    @Before(value = "cutService()")
    public void doBefore(JoinPoint point) {
        try {
            handle(point, null);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }
    }

    @Around("cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        Object result = point.proceed();
        try {
            handle(point, result);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }
        return result;
    }


    private void handle(JoinPoint point, Object result) throws Exception {

        // 校验注解用户方法
        Signature sig = ReflectUitl.validateUseMethod(point);

        //获取被注解的方法对象
        Method currentMethod = ReflectUitl.getMethodName(point, (MethodSignature) sig);
        String methodName = currentMethod.getName();

        //获取注解对象本身
        PrintTimeGapAnnocation annotation = currentMethod.getAnnotation(PrintTimeGapAnnocation.class);
//        String bussinessName = annotation.value();//注解描述的业务

        // 获取拦截方法的参数
        Object[] params = point.getArgs();

        Logger log = LoggerFactory.getLogger(point.getTarget().getClass());

        Object param = params[0];
        Order order = (Order) param;
        long courierWaitKitchenTime = order.getCourierWaitKitchenTime();

        long kitchenWaitCourierTime = order.getKitchenWaitCourierTime();

        if (kitchenWaitCourierTime == 0 && courierWaitKitchenTime == 0) {
            return;
        }
        if (courierWaitKitchenTime > 0) {
            log.info("\norderid:{},\n courier wait time {}(milliseconds) between arrival and order pickup", order.getId(), courierWaitKitchenTime);
        }
        if (kitchenWaitCourierTime > 0) {
            log.info("\norderid:{},\n food wait time {}(milliseconds) between order ready and pickup", order.getId(), kitchenWaitCourierTime);
        }

    }

}
