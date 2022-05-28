package com.cloudkitchens.orderdelivery.enums;

public enum RateLimitTypeEnum {
    COUNTER("counter"),
    LEAPARRAY("leapArray"),//ali--sentinal
    LEAKBUCKET("leakBucket"),//
    TOKENBUCKET("TokenBucket");//
    private String type;

    private RateLimitTypeEnum(String type) {
        this.type = type;
    }
}