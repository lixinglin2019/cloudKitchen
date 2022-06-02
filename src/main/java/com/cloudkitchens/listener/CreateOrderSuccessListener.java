package com.cloudkitchens.listener;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import com.cloudkitchens.event.CreateOrderSuccessEvent;
import com.cloudkitchens.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CreateOrderSuccessListener implements ApplicationListener<CreateOrderSuccessEvent> {

    @Autowired
    private KitchenService cloudKitchenService;
    @Override
    public void onApplicationEvent(CreateOrderSuccessEvent event) {
        Order order = event.getOrder();
        LinkedBlockingQueue orderQueue = OrderQueueEum.ORDER_QUEUE.getOrderQueue();


        //订单入队
        orderQueue.add(order);//非阻塞，有异常返回

//        原先是同步操作--现在改为异步
//        //触发消费队列
//        cloudKitchenService.consumerOrderQueue();

    }
}
