package com.cloudkitchens.service;

import com.cloudkitchens.SystemStartEvent;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.PropertyUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CourierService {

    private volatile AtomicInteger courierId = new AtomicInteger(0);

    /**
     * 只有在MATCH策略才会有这种情况，FIFO策略没有这个延迟队列，因为他们都直接阻塞等待
     * 快递延时队列中用户
     * 消费 readQueue
     */
    public void delayCourierConsumeReadyQueue() {
        AtomicInteger courierPickUpOrderNumer = CourierQueueEnum.COURIER_QUEUE.courierPickUpOrderNumer;
        new Thread(() -> {
            while (courierPickUpOrderNumer.get() < SystemStartEvent.orderTotalNumber) {
                //消费
                Object courior = SystemStartEvent.consumeMap.get(ConsumerEnum.COURIER_DELAY_CONSUMER).consume();
                if (courior == null) {
                    return;
                }

                //消费
                Object food = SystemStartEvent.consumeMap.get(ConsumerEnum.KITCHEN_READY_CONSUMER).consume();
                if (food == null) {
                    return;
                }
                int andIncrement = courierPickUpOrderNumer.getAndIncrement();
                if (andIncrement == SystemStartEvent.orderTotalNumber-1) {
                    OrderUtil.pintOrderCache();
                }

                ReadyDTO peek = (ReadyDTO) food;
                Order order = peek.getOrder();//只是取，不删除--说明餐也准备好了
                order.setOrderPickUpTime(System.currentTimeMillis());
                OrderUtil.printTimegap(order);
                OrderUtil.setTimeGap(order);
            }
        }).start();


    }

    /**
     * 快递员pickup
     * <p>
     * 根据策略做了区分
     * <p>
     * 刚到的快递员取餐（消费readyQueue）
     * 其实就是pickup 取餐
     */
    public void arriveCourierConsumeReadyQueue() {
        //根据策略来区分---
        String dispathCourierStrategy = PropertyUtils.getDispathCourierProp("dispathCourier.strategy");

        SystemStartEvent.dispatchCourierStrategyMap.get(dispathCourierStrategy).couriorConsumeReadyQueue();

    }

    /**
     * 创建快递员
     *
     * @return
     */
    public Courier createCourier() {
        Courier courier = new Courier();
        int couerierId = courierId.incrementAndGet();
        String id = String.valueOf(couerierId);
        courier.setId(id);
        //并且该用户，放入所有用户列表中
        CourierQueueEnum.COURIER_QUEUE.allCouriers.add(courier);
        return courier;
    }
}
