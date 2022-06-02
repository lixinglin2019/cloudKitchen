package com.cloudkitchens.aop;

import com.cloudkitchens.aop.annotation.OutputEventsAnnocation;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 日志打印
 */
@Aspect
@Component
public class OutputEventsAop {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value = "@annotation(com.cloudkitchens.aop.annotation.OutputEventsAnnocation)")
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


        Signature sig = ReflectUitl.validateUseMethod(point);

        //获取被注解的方法对象
        Method currentMethod = ReflectUitl.getMethodName(point, (MethodSignature) sig);

        String methodName = currentMethod.getName();

        //获取注解对象本身
        OutputEventsAnnocation annotation = currentMethod.getAnnotation(OutputEventsAnnocation.class);
//        String bussinessName = annotation.value();//注解描述的业务

        // 获取拦截方法的参数
        Object[] params = point.getArgs();
        String description = annotation.description();//什么时间

        Logger log = LoggerFactory.getLogger(point.getTarget().getClass());

        Object param = params[0];
        Long time = (long) param;
        Object param1 = params[1];

        Field id = result.getClass().getDeclaredField("id");
        String idStr = (String) id.get(result);//
        System.out.println(idStr);
        String orderId = (String) param1;


//        log.info("orderId:{}\n{},\n {}(milliseconds) ",orderId, description, time);


    }

}
