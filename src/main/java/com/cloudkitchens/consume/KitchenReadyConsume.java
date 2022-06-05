package com.cloudkitchens.consume;

import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.DelayQueue;

@Component
public class KitchenReadyConsume implements IConsume {
    @Override
    public ConsumerEnum type() {
        return ConsumerEnum.KITCHEN_READY_CONSUMER;
    }

    @Override
    public Object consume() {

        DelayQueue delayQueue = (DelayQueue) queue();
        ReadyDTO readyDTO = null;
        try {
            readyDTO = (ReadyDTO) delayQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " 消费（餐厅）制作完成订单:" + readyDTO + ",现在完成订单数=" + queue().size());
        return readyDTO;
    }

    @Override
    public Queue queue() {
        return KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
    }
}
