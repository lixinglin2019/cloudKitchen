package com.cloudkitchens.service;

import com.cloudkitchens.aop.annotation.CounterRateLimitAnnocation;
import com.cloudkitchens.dao.OrderDao;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.event.CreateOrderExceptionEvent;
import com.cloudkitchens.event.CreateOrderSuccessEvent;
import com.cloudkitchens.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ApplicationContext applicationContext;

    private static volatile AtomicInteger orderId = new AtomicInteger(0);

    public static Logger log = LoggerFactory.getLogger(OrderService.class);


    @CounterRateLimitAnnocation(timeLimit = 1, numLimit = 100)
    public void createOrder() {
        Order order = new Order();
        order.setId(String.valueOf(orderId.incrementAndGet()));
        order.setName("pizza");
        order.setPrepTime(8);
        try {

            orderDao.save(order);
            String formatData = TimeUtil.getFormatData(null);
            log.info("orderid:{}  order created     at  {} ", order.getId(), formatData);


            //event-bus 观察者模式 异步解耦
            applicationContext.publishEvent(new CreateOrderSuccessEvent("", order));
        } catch (Exception e) {
            e.printStackTrace();
            applicationContext.publishEvent(new CreateOrderExceptionEvent("", order, e.getMessage()));
        }
    }


}
