package com.cloudkitchens.consume;

import com.cloudkitchens.dto.CourierDelayDTO;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

@Component
public class CourierDelayConsume implements IConsume {
    @Override
    public ConsumerEnum type() {
        return ConsumerEnum.COURIER_DELAY_CONSUMER;
    }

    @Override
    public Object consume() {
        CourierDelayDTO courierDelayDTO = null;
        DelayQueue delayQueue = (DelayQueue) queue();
        try {
            Delayed take = delayQueue.take();
            courierDelayDTO = (CourierDelayDTO) take;
            System.out.println(Thread.currentThread().getName() + " 消费（快递员延迟取餐）队列:" + courierDelayDTO + ",现在(快递员延迟取餐队列)快递员数=" + queue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return courierDelayDTO;

    }

    @Override
    public Queue queue() {
        return CourierQueueEnum.COURIER_QUEUE.courierdelayQueue;
    }
}
