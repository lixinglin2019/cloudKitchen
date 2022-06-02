package com.cloudkitchens.strategy.ratelimit;

import java.util.Map;

public interface IRateLimit {
    public String getType();
    //返回true--通过 false-被限流
    public Boolean rateLimit(Map params);
}
