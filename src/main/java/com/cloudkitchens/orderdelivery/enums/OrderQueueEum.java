package com.cloudkitchens.orderdelivery.enums;

import com.cloudkitchens.orderdelivery.domain.order.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public enum OrderQueueEum {
    ORDER_QUEUE_EUM;
    private LinkedBlockingQueue orderQueue = new LinkedBlockingQueue();

    //为了统计打印时间--订单完成时间
    private Map<Order,Long> orderMakeFinishTimeMap = new HashMap<>();


    private OrderQueueEum() {
    }

    public LinkedBlockingQueue getOrderQueue() {
        return orderQueue;
    }

    public Map<Order, Long> getOrderMakeFinishTimeMap() {
        return orderMakeFinishTimeMap;
    }

}
