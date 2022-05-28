package com.cloudkitchens.orderdelivery.enums;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.dto.CourierDto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public enum CourierArriveQueueEnum {
    COURIER_ARRIVE_QUEUE_ENUM;

    //因为取餐不一定是按照队列来的，异步导致可能后来的人先取到餐，又因为 如果同等优先级需要排队取餐，所有它应该用链表的数据结构

    private  LinkedList<CourierDto> courierArriveLinkedList = new LinkedList<>();

    private Map<Order,Long> orderPickupTimeMap = new HashMap<>();

    private Map<String, List<Order>> courierOrderList = new HashMap<>();
    private CourierArriveQueueEnum() {

    }
    //为了统计打印时间--订单取餐时间
    public Map<Order, Long> getOrderPickupTimeMap() {
        return orderPickupTimeMap;
    }

    public LinkedList<CourierDto> getCourierArriveLinkedList() {
        return courierArriveLinkedList;
    }

    public Map<String, List<Order>> getCourierOrderList() {
        return courierOrderList;
    }
}
