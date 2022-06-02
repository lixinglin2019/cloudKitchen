package com.cloudkitchens.enums;

public enum OrderStateEnum {
    OPEN(1),//下单

    KITCHEN_RECEIVED(2),
    KITCHEN_MAKING(3),
    KITCHEN_FINISHED(4),

    DELIVERY_GET(5),
    DELIVERY_ARRIVED(6),
    DELIVERY_SENDING(7),
    DELIVERY_FINISH(8);

    public Integer state;

    private OrderStateEnum(Integer status) {
        this.state = status;
    }

}
