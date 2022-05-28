package com.cloudkitchens.orderdelivery.strategy.dispatchCourier;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;
import com.cloudkitchens.orderdelivery.enums.DispatchCourierStrategyEnum;

import java.util.concurrent.LinkedBlockingQueue;

public class LRUDispatchCourier implements IDispatchCourier{
    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.LRU.name();
    }

    @Override
    public void dispatchCourier(LinkedBlockingQueue<Order> orders, LinkedBlockingQueue<CourierUser> courierUsers) {
        throw new  UnsupportedOperationException("unimpl");
    }
}
