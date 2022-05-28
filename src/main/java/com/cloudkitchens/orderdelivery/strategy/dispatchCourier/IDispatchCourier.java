package com.cloudkitchens.orderdelivery.strategy.dispatchCourier;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;

import java.util.concurrent.LinkedBlockingQueue;

public interface IDispatchCourier {
    public String getType();
    public void dispatchCourier(LinkedBlockingQueue<Order> orders, LinkedBlockingQueue<CourierUser> courierUsers);
}
