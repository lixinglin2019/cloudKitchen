package com.cloudkitchens.orderdelivery.service;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;
import com.cloudkitchens.orderdelivery.dto.CourierDto;
import com.cloudkitchens.orderdelivery.enums.CourierArriveQueueEnum;
import com.cloudkitchens.orderdelivery.enums.OrderQueueEum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class DeliveryService {

    public static List<CourierUser> courierUsers = new ArrayList<>();

    public static List<CourierUser> getCourierUsers() {
        return courierUsers;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 8; i++) {//安排8个快递员
            CourierUser courierUser = new CourierUser();
            courierUser.setId(i + "");
            courierUsers.add(courierUser);

        }
    }

    /**
     * TODO 模拟快递员相继到达
     * 3~15秒用random随机数
     * 写入队列 queue中
     */

    public void curiorArriveRandom() {

        TreeMap treeMap = new TreeMap();
        for (CourierUser courierUser : courierUsers) {
            //产生一个3~15之间的随机数 (数据类型)(最小值+Math.random()*(最大值-最小值+1))
            Long arriveTime = Long.parseLong(3 + Math.random() * (15 - 3 + 1) * 1000 + "");//毫秒
            long arriveTimeMillis = System.currentTimeMillis() + arriveTime;//当前时间顺延
            treeMap.put(courierUser, arriveTimeMillis);
        }


        //TODO 对value排序
        ArrayList<Map.Entry<CourierUser, Long>> list = new ArrayList<Map.Entry<CourierUser, Long>>(treeMap.entrySet());
        //升序排序
        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));


        Map<String, List<Order>> courierOrderList = CourierArriveQueueEnum.COURIER_ARRIVE_QUEUE_ENUM.getCourierOrderList();


        //把结果放入队列中---考虑优先级队列？
        //treeMap快递，几点到的
        // 快递，手里拿着哪些订单
        for (Map.Entry<CourierUser, Long> courierUserStringEntry : list) {
            CourierUser courierUser = courierUserStringEntry.getKey();
            Long arriveTimeMillis = courierUserStringEntry.getValue();
            List<Order> orders = courierOrderList.get(courierUser.getId());
            CourierDto courierDto = new CourierDto();
            courierDto.setCourierUser(courierUser);
            courierDto.setArriveTime(arriveTimeMillis);
            courierDto.setOrderList(orders);

            CourierArriveQueueEnum.COURIER_ARRIVE_QUEUE_ENUM.getCourierArriveLinkedList().add(courierDto);

        }

//        --------match策略的算法--------绑定死  订单和人  的关系
        pickupOrder();//拣货
    }

    //取餐
    public void pickupOrder() {
        //快递员已到达-队列
        LinkedList<CourierDto> courierArriveLinkedList = CourierArriveQueueEnum.COURIER_ARRIVE_QUEUE_ENUM.getCourierArriveLinkedList();
        Map<Order, Long> orderPickupTimeMap = CourierArriveQueueEnum.COURIER_ARRIVE_QUEUE_ENUM.getOrderPickupTimeMap();
        //订单已制作完成-队列
        Map<Order, Long> orderMakeFinishTimeMap = OrderQueueEum.ORDER_QUEUE_EUM.getOrderMakeFinishTimeMap();

        for (CourierDto courierDto : courierArriveLinkedList) {
            List<Order> orderList = courierDto.getOrderList();
            for (Order order : orderList) {
                Long orderMakFinishTime = orderMakeFinishTimeMap.get(order);//制作完成时间
                if (orderMakFinishTime == null) {//没制作完成

                    continue;//接着看第二单有没有制作完成
                } else {//制作完成了

                    //记录取餐时间
                    long pickupTime = System.currentTimeMillis();
                    System.out.println("餐厅完成订单时间："+orderMakFinishTime+" 等待快递到店取餐时间："+pickupTime+"  共耗时："+(pickupTime-orderMakFinishTime));
                    orderPickupTimeMap.put(order, pickupTime);
                    orderMakeFinishTimeMap.remove(order);
                }
            }
        }
    }

}
