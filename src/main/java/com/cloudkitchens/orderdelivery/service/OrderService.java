package com.cloudkitchens.orderdelivery.service;

import com.cloudkitchens.orderdelivery.aop.annotation.CounterRateLimitAnnocation;
import com.cloudkitchens.orderdelivery.dao.CloudKitchenDao;
import com.cloudkitchens.orderdelivery.dao.OrderDao;
import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.event.CreateOrderExceptionEvent;
import com.cloudkitchens.orderdelivery.event.CreateOrderSuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CloudKitchenDao cloudKitchenDao;

    @Autowired
    private ApplicationContext applicationContext;






    @CounterRateLimitAnnocation(timeLimit = 1,numLimit = 2)
    public void createOrder(Order order) throws Exception {
        try {
            orderDao.save(order);
            //event-bus 观察者模式 异步解耦
            applicationContext.publishEvent(new CreateOrderSuccessEvent("", order));
        } catch (Exception e) {
            e.printStackTrace();
            applicationContext.publishEvent(new CreateOrderExceptionEvent("", order, e.getMessage()));
        }
    }




}
