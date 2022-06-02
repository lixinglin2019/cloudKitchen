package com.cloudkitchens.strategy.dispatchCourier;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;

public interface IDispatchCourier {
    public String getType();
    public void disppatchCourier(Order order, Courier courier);

    //不同策略，消费队列的情况不同，抽象一个接口
    public void couriorConsumeReadyQueue();
}
