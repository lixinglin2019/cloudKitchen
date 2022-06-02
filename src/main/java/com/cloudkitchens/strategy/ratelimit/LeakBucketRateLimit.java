package com.cloudkitchens.strategy.ratelimit;

import com.cloudkitchens.enums.RateLimitTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LeakBucketRateLimit implements IRateLimit {
    @Override
    public String getType() {
        return RateLimitTypeEnum.LEAKBUCKET.name();
    }

    @Override
    public Boolean rateLimit(Map params) {

        return Boolean.FALSE;
    }
}
