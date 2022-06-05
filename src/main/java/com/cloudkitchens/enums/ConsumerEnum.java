package com.cloudkitchens.enums;

public enum ConsumerEnum {
    ORDER_CONSUMER("ORDER_CONSUMER"),
    KITCHEN_RECEIVE_CONSUMER("KITCHEN_RECEIVE_CONSUMER"),
    KITCHEN_READY_CONSUMER("KITCHEN_READY_CONSUMER"),
    COURIER_ARRIVED_CONSUMER("COURIER_ARRIVED_CONSUMER"),
    COURIER_DELAY_CONSUMER("COURIER_DELAY_CONSUMER"),;

    private String name;

    private ConsumerEnum(String name) {
        this.name = name;
    }
}
