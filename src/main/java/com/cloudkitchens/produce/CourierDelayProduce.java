package com.cloudkitchens.produce;

import com.cloudkitchens.dto.CourierDelayDTO;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.DelayQueue;

@Component
public class CourierDelayProduce implements Iproduce {
    @Override
    public ProducerEnum type() {
        return ProducerEnum.COURIER_DELAY_PRODUCER;
    }

    @Override
    public void produce(Object object) {

        DelayQueue delayQueue = (DelayQueue) queue();
        CourierDelayDTO courierDelayDTO = (CourierDelayDTO) object;
        delayQueue.put(courierDelayDTO);

        System.out.println(Thread.currentThread().getName() + " 生产（快递员延迟）队列:" + courierDelayDTO + ",现在(快递员延迟队列)快递员数=" + queue().size());


    }

    @Override
    public void produce(Queue queue, Object object) {

    }

    @Override
    public Queue queue() {
        return CourierQueueEnum.COURIER_QUEUE.courierdelayQueue;
    }
}
