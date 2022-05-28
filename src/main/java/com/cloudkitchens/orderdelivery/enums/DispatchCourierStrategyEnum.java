package com.cloudkitchens.orderdelivery.enums;

public enum DispatchCourierStrategyEnum {
    MATCH("match"),
    FIFO("fifo"),
    LRU("lru");
    private String strategy;

    private DispatchCourierStrategyEnum(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }
}
