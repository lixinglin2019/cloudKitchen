package com.cloudkitchens.consume;

import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class CourierArriveConsume implements IConsume {
    @Override
    public ConsumerEnum type() {
        return ConsumerEnum.COURIER_ARRIVED_CONSUMER;
    }

    @Override
    public Object consume() {
        PriorityBlockingQueue queue = (PriorityBlockingQueue)queue();
        Object take = null;
        try {
            take = queue.take();//阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CourierArriveDTO courierArriveDTO = (CourierArriveDTO) take;
        System.out.println(Thread.currentThread().getName() + " 消费（快递员到达）队列:" + courierArriveDTO + ",现在快递员到达数=" + queue.size());

        return courierArriveDTO;

    }

    @Override
    public Queue queue() {
        return CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;
    }
}
