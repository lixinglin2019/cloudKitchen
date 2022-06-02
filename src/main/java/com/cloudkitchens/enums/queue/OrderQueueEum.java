package com.cloudkitchens.enums.queue;

import com.cloudkitchens.domain.order.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public enum OrderQueueEum {
    ORDER_QUEUE;
    private Map<String, Order> orderCache = new ConcurrentHashMap<>();
    /**
     * 创建的订单都放在这个队列
     */
    private LinkedBlockingQueue orderQueue = new LinkedBlockingQueue(10);//队列中积攒的数据超过10个，则会自动扩容


    private OrderQueueEum() {
    }

    public LinkedBlockingQueue getOrderQueue() {
        return orderQueue;
    }

    public Map<String, Order> getOrderCache() {
        return orderCache;
    }
}
