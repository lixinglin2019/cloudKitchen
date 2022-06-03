package com.cloudkitchens.service;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierDelayDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.strategy.dispatchCourier.IDispatchCourier;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.PropertyUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CourierService {
    private volatile AtomicInteger courierId = new AtomicInteger(0);

    Map<String, IDispatchCourier> dispatchCourierStrategyMap = new HashMap<>();

    public CourierService(List<IDispatchCourier> dispatchCourierList) {
        for (IDispatchCourier iDispatchCourier : dispatchCourierList) {
            dispatchCourierStrategyMap.put(iDispatchCourier.getType(), iDispatchCourier);
        }
    }


    /**
     * 快递延时队列中用户
     * 消费 readQueue
     */
    public synchronized void delayCourierConsumeReadyQueue() {

        DelayQueue<CourierDelayDTO> courierDelayQueue = CourierQueueEnum.COURIER_QUEUE.courierdelayQueue;
        CourierDelayDTO courier2OrderDelayDto = null;//延迟队列中有值
        try {
            courier2OrderDelayDto = courierDelayQueue.take();
            System.out.println(Thread.currentThread().getName() + " 消费（快递员延迟取餐）队列:" + courier2OrderDelayDto + ",现在(快递员延迟取餐)数=" + courierDelayQueue.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (courier2OrderDelayDto == null) {
            return;
        }
        DelayQueue<ReadyDTO> readyQueue = KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
        ReadyDTO peek = null;//
        try {
            peek = readyQueue.take();//阻塞
            System.out.println(Thread.currentThread().getName() + " 生产（餐厅）制作完成订单:" + peek + ",现在接单数=" + readyQueue.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Order readyOrder = peek.getOrder();//只是取，不删除--说明餐也准备好了
        if (readyOrder == null) {
            return;
        }
        //消费完后，把队列中对象删除
        delayCouriorPickupReadyQueue(readyOrder, courierDelayQueue, courier2OrderDelayDto, readyQueue);
    }


    /**
     * 快递员pickup
     *
     * 根据策略做了区分
     *
     * 刚到的快递员取餐（消费readyQueue）
     * 其实就是pickup 取餐
     */
    public void arriveCourierConsumeReadyQueue() {
        //根据策略来区分---
        String dispathCourierStrategy = PropertyUtils.getDispathCourierProp("dispathCourier.strategy");
        dispatchCourierStrategyMap.get(dispathCourierStrategy).couriorConsumeReadyQueue();
    }

    public Courier createCourier() {
        Courier courier = new Courier();
        int couerierId = courierId.incrementAndGet();
        String id = String.valueOf(couerierId);
        courier.setId(id);
        //并且该用户，放入所有用户列表中
        CourierQueueEnum.COURIER_QUEUE.allCouriers.add(courier);
        return courier;

    }


    private void delayCouriorPickupReadyQueue(Order order, DelayQueue<CourierDelayDTO> courierDelayQueue, CourierDelayDTO courier2OrderDelayDto, DelayQueue<ReadyDTO> readyQueue) {
        order.setOrderPickUpTime(System.currentTimeMillis());

        OrderUtil.printTImeGap(order);
        OrderUtil.setTimeGap(order);
//        readyQueue.remove(order);
//        courierDelayQueue.remove(courier2OrderDelayDto);
    }



}
