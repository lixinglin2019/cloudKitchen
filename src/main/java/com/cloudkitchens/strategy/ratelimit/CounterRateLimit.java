package com.cloudkitchens.strategy.ratelimit;


import com.cloudkitchens.enums.RateLimitTypeEnum;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class CounterRateLimit implements IRateLimit {

    /**
     * 接口限流实现 ，根据API名称，
     * 每5秒最多只能请求指定的次数（如60次），超过限制则这分钟内返回错误，但5秒后又可以正常请求。 接口定义
     * 对invoke方法进行调用，超过限制则return false
     */
    private static Map<String, CounterRateLimit> cache = new ConcurrentHashMap<String, CounterRateLimit>();

    /**
     * 每秒接收2个订单
     */
    private volatile long time;
    private volatile int invokeNum;


    @Override
    public String getType() {
        return RateLimitTypeEnum.COUNTER.name();
    }


    @Override
    public Boolean rateLimit(Map map) {
        String apiName = (String) map.get("apiName");
        Integer timeLimit = (Integer) map.get("timeLimit");
        Integer numLimit = (Integer) map.get("numLimit");

        if (apiName == null) {
            return false;
        }
        CounterRateLimit cacheValidate = null;
        synchronized (cache) {//并发度低
            cacheValidate = cache.get(apiName);
            if (cacheValidate == null) {
                cacheValidate = new CounterRateLimit();
                cacheValidate.setTime(System.currentTimeMillis() / 1000 + timeLimit);
                cacheValidate.setInvokeNum(1);
                cache.put(apiName, cacheValidate);
//                cache.putIfAbsent(apiName,cacheValidate);//这样写 就不需要synchronized了
                return true;
            }
            return cacheValidate.isValidate(timeLimit, numLimit);
        }

    }


    public boolean isValidate(Integer timeLimit, Integer numLimit) {
        this.invokeNum = invokeNum + 1;
        //只有下面两种情况通过，其余的都不通过！被限流
        if (System.currentTimeMillis() / 1000 <= time) {
            if (invokeNum <= numLimit) {
                return true;
            }
        } else {
            this.invokeNum = 1;
            this.time = System.currentTimeMillis() / 1000 + timeLimit;
            return true;
        }
        return false;
    }
}
