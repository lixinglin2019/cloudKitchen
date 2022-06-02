package com.cloudkitchens.strategy.dispatchCourier;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;

public class LRUDispatchCourier implements IDispatchCourier{
    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.LRU.name();
    }

    @Override
    public void disppatchCourier(Order order, Courier courier) {
        throw new  UnsupportedOperationException("unimpl");
    }

    @Override
    public void couriorConsumeReadyQueue() {
        throw new UnsupportedOperationException("unsupported operation");
    }


}
