package com.cloudkitchens.enums.queue;

import com.cloudkitchens.domain.order.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public enum OrderQueueEum {
    ORDER_QUEUE;
    private static volatile AtomicInteger orderNumer = new AtomicInteger(0);




    public Map<String, Order> orderCache = new ConcurrentHashMap<>();
    /**
     * 创建的订单都放在这个队列
     */
    public LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue(10);//队列中积攒的数据超过10个，则会自动扩容


    private OrderQueueEum() {
    }

}
