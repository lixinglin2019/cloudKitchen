package com.cloudkitchens.orderdelivery.strategy.ratelimit;

import com.cloudkitchens.orderdelivery.enums.RateLimitTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenBucketRateLimit implements IRateLimit {
    @Override
    public String getType() {
        return RateLimitTypeEnum.TOKENBUCKET.name();
    }

    @Override
    public Boolean rateLimit(Map params) {
        return Boolean.FALSE;
    }
}
