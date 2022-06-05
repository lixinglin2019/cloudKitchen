package com.cloudkitchens.enums;

public enum ProducerEnum {
    ORDER_PRODUCER("ORDER_PRODUCER"),
    KITCHEN_RECEIVE_PRODUCER("KITCHEN_RECEIVE_PRODUCER"),
    KITCHEN_READY_PRODUCER("KITCHEN_READY_PRODUCER"),
    COURIER_ARRIVED_PRODUCER("COURIER_ARRIVED_PRODUCER"),
    COURIER_DELAY_PRODUCER("COURIER_DELAY_PRODUCER"),;

    private String name;

    private ProducerEnum(String name) {
        this.name = name;
    }
}
