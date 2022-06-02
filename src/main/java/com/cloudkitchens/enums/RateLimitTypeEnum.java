package com.cloudkitchens.enums;

public enum RateLimitTypeEnum {
    COUNTER("COUNTER"),
    LEAPARRAY("LEAPARRAY"),//ali--sentinal
    LEAKBUCKET("LEAKBUCKET"),//
    TOKENBUCKET("TOKENBUCKET");//
    private String type;

    private RateLimitTypeEnum(String type) {
        this.type = type;
    }
}