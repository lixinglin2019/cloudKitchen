package com.cloudkitchens.orderdelivery.service;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.dto.KitchenDto;
import com.cloudkitchens.orderdelivery.enums.OrderQueueEum;
import com.cloudkitchens.orderdelivery.event.CloudKitchenReceiveOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class CloudKitchenService {

    @Autowired
    private ApplicationContext applicationContext;




    public void consumerOrderQueue() {
        /**
         * //在这里把队头元素取出并删除--同时在这里通知快递员，
         * 是考虑事务的关联性，二者有关联关系（或者说优先级，如果厨房都没有消费到消息，那么快递就算消费到也是没有意义的）
         */
        try {
            LinkedBlockingQueue orderQueue = OrderQueueEum.ORDER_QUEUE_EUM.getOrderQueue();//单例模式
            Order order = (Order) orderQueue.take();//阻塞等待  从订单队列取出来  --->放入 等待制作队列

            KitchenDto kitchenDto = KitchenDto.getInstance();
            LinkedBlockingQueue waitForMakeQueue = kitchenDto.getWaitForMakeQueue();
            waitForMakeQueue.put(order);//餐厅接单

            //异步解耦
            ApplicationEvent applicationEvent = new CloudKitchenReceiveOrderEvent("",order);
            applicationContext.publishEvent(applicationEvent);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
