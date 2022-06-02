package com.cloudkitchens.enums;

public enum DispatchCourierStrategyEnum {
    MATCH("MATCH"),
    FIFO("FIFO"),
    LRU("LRU");
    private String strategy;

    private DispatchCourierStrategyEnum(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return strategy;
    }
}
