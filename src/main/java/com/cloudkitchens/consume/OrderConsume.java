package com.cloudkitchens.consume;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class OrderConsume implements IConsume {
    @Override
    public ConsumerEnum type() {
        return ConsumerEnum.ORDER_CONSUMER;
    }

    @Override
    public Object consume() {
        LinkedBlockingQueue linkedBlockingQueue = (LinkedBlockingQueue) queue();
        Object poll = null;
        try {
            poll = linkedBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if (poll == null) {//暂时没订单，则直接返回
//            return null;
//        }
        Order order = (Order) poll;
        System.out.println(Thread.currentThread().getName() + " 消费订单:" + order + ",现在剩余的订单数=" + queue().size());

        return order;
    }

    @Override
    public Queue queue() {
        return OrderQueueEum.ORDER_QUEUE.orderQueue;
    }
}
