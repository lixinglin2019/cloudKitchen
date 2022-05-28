package com.cloudkitchens.orderdelivery.listener;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.enums.OrderQueueEum;
import com.cloudkitchens.orderdelivery.event.CreateOrderSuccessEvent;
import com.cloudkitchens.orderdelivery.service.CloudKitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CreateOrderSuccessListener implements ApplicationListener<CreateOrderSuccessEvent> {

    @Autowired
    private CloudKitchenService cloudKitchenService;
    @Override
    public void onApplicationEvent(CreateOrderSuccessEvent event) {
        Order order = event.getOrder();
        LinkedBlockingQueue orderQueue = OrderQueueEum.ORDER_QUEUE_EUM.getOrderQueue();

        //订单入队
        orderQueue.add(order);//非阻塞，有异常返回
        //触发消费队列
        cloudKitchenService.consumerOrderQueue();
        //TODO 其他事件
    }
}
