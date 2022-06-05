package com.cloudkitchens.produce;

import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class CourierArrivedProduce implements Iproduce {
    @Override
    public ProducerEnum type() {
        return ProducerEnum.COURIER_ARRIVED_PRODUCER;
    }

    @Override
    public void produce(Object object) {
        PriorityBlockingQueue priorityBlockingQueue = (PriorityBlockingQueue) queue();


        CourierArriveDTO courierArriveDTO = (CourierArriveDTO) object;
        priorityBlockingQueue.put(courierArriveDTO); //阻塞插入

        System.out.println(Thread.currentThread().getName() + " 生产（快递员到达）队列:" + courierArriveDTO + ",现在快递员到达数=" + queue().size());

    }

    @Override
    public void produce(Queue queue, Object object) {

    }

    @Override
    public Queue queue() {
        return CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;
    }
}
