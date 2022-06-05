package com.cloudkitchens.produce;

import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.DelayQueue;

@Component
public class KitchenReadyProduce implements Iproduce {
    @Override
    public ProducerEnum type() {
        return ProducerEnum.KITCHEN_READY_PRODUCER;
    }

    @Override
    public void produce(Object object) {
        if (object == null) {
            return;
        }
        DelayQueue delayQueue = (DelayQueue) queue();
        ReadyDTO readyDTO = (ReadyDTO) object;
        delayQueue.put(readyDTO);
        System.out.println(Thread.currentThread().getName() + " 生产（餐厅）制作完成订单:" + readyDTO + ",现在完成订单数=" + queue().size());

    }

    @Override
    public void produce(Queue queue, Object object) {

    }

    @Override
    public Queue queue() {
        return KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
    }
}
