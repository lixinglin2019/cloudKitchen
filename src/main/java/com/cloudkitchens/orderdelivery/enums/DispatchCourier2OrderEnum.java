package com.cloudkitchens.orderdelivery.enums;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DispatchCourier2OrderEnum {
    DISPATCH_COURIER_2_ORDER_ENUM;
    private Map<Order, CourierUser> dispatchCourier2OrderMap = new ConcurrentHashMap<>();

    private DispatchCourier2OrderEnum() {
    }

    public Map<Order, CourierUser> getDispatchCourier2OrderMap() {
        return dispatchCourier2OrderMap;
    }


}
