package com.cloudkitchens.orderdelivery.strategy.ratelimit;

import com.cloudkitchens.orderdelivery.enums.RateLimitTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LeapArrayRateLimit implements IRateLimit {
    @Override
    public String getType() {
        return RateLimitTypeEnum.LEAPARRAY.name();
    }

    @Override
    public Boolean rateLimit(Map params) {
        return Boolean.FALSE;
    }
}
