package com.cloudkitchens.enums.queue;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.dto.ReadyDTO;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public enum KitchenQueueEnum {


    KITCHEN_QUEUE;
//    private Map<Order, CourierUser> dispatchCourier2OrderMap = new ConcurrentHashMap<>();

    public volatile AtomicInteger kitchenReceiveOrderNumber = new AtomicInteger(0);
    public volatile AtomicInteger kitchenReceiveReadyOrderNumber = new AtomicInteger(0);



    //每个餐厅都有两个队列，1个是保存接收的订单 1个是保存已经处理好的订单
    //餐厅承接订单队列
    public LinkedBlockingQueue<Order> receiveQueue = new LinkedBlockingQueue(10);;
    //餐厅制作完成队列
    public DelayQueue<ReadyDTO> readyQueue = new DelayQueue<ReadyDTO>();

    private KitchenQueueEnum() {
    }


}
