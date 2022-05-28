package com.cloudkitchens.orderdelivery.strategy.dispatchCourier;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;
import com.cloudkitchens.orderdelivery.enums.DispatchCourierStrategyEnum;

import java.util.concurrent.LinkedBlockingQueue;

public class FIFODispatchCourier implements IDispatchCourier {

    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.FIFO.name();
    }

    /**
     * 快递员到达后，
     *a courier picks up the next available order upon arrival.
     * If there are multiple orders available,
     * pick up an arbitrary order.
     * If there are no available orders, couriers wait for the next available one.
     * When there are multiple couriers waiting,
     * the next available order is assigned to the earliest arrived courier.
     * @param finishMakeQueue
     * @param hasArrivedCourierUsers
     */
    @Override
    public void dispatchCourier(LinkedBlockingQueue<Order> finishMakeQueue, LinkedBlockingQueue<CourierUser> hasArrivedCourierUsers) {
        int size = finishMakeQueue.size();
        if (size>0){
            Order order = null;
            try {
                order = finishMakeQueue.take();//pickup finished order
                CourierUser courierUser = hasArrivedCourierUsers.take();
                order.setCourierId(courierUser.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
