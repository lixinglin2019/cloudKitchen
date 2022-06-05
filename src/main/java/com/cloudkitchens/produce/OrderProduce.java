package com.cloudkitchens.produce;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class OrderProduce<T> implements Iproduce {

    @Override
    public ProducerEnum type() {
        return ProducerEnum.ORDER_PRODUCER;
    }

    @Override
    public void produce(Object object) {//
        if (object == null) {
            return;
        }


        //队列中设置值
        Order order = (Order) object;
        LinkedBlockingQueue queue = (LinkedBlockingQueue) queue();
        try {
            queue.put(order);//阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 生产（订单）队列:" + order + ",现在订单数=" + queue().size());

    }

    @Override
    public void produce(Queue queue, Object object) {

    }

    @Override
    public Queue queue() {
        return OrderQueueEum.ORDER_QUEUE.orderQueue;
    }
}
