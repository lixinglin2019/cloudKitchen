package com.cloudkitchens.consume;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class KitchenReceiveConsume implements IConsume {
    @Override
    public ConsumerEnum type() {
        return ConsumerEnum.KITCHEN_RECEIVE_CONSUMER;
    }

    @Override
    public Order consume() {
        LinkedBlockingQueue linkedBlockingQueue = (LinkedBlockingQueue) queue();
        Object poll = null;
        try {
            poll = linkedBlockingQueue.take();//阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Order order = (Order) poll;
        System.out.println(Thread.currentThread().getName() + " 消费（餐厅）接单:" + order + ",现在接单数=" + queue().size());
        return order;
    }

    @Override
    public Queue queue() {
        return KitchenQueueEnum.KITCHEN_QUEUE.receiveQueue;
    }
}
