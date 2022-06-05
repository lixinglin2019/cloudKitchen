package com.cloudkitchens.produce;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class KitchenReceiveProduce<T> implements Iproduce {

    @Override
    public ProducerEnum type() {
        return ProducerEnum.KITCHEN_RECEIVE_PRODUCER;
    }

    @Override
    public void produce(Object object) {//
        if (object == null) {
            return;
        }
        //队列中设置值
        Order order = (Order) object;
        try {
            //这里每次第二次添加，第一次添加的就丢了，又变成0了！！---
//            fix这个bug
            ((LinkedBlockingQueue)this.queue()).put(order);
            System.out.println(Thread.currentThread().getName() + " 生产（餐厅）接单:" + object + ",现在接单数=" + queue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void produce(Queue queue,Object object) {//
        if (object == null) {
            return;
        }
        //队列中设置值
        Order order = (Order) object;
        try {
            //这里每次第二次添加，第一次添加的就丢了，又变成0了！！---
//            fix这个bug
            ((LinkedBlockingQueue)queue).put(order);
            System.out.println(Thread.currentThread().getName() + " 生产（餐厅）接单:" + object + ",现在接单数=" + queue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Queue queue() {
        return KitchenQueueEnum.KITCHEN_QUEUE.receiveQueue;
    }
}
