package com.cloudkitchens.strategy.consumerQueue;

import com.cloudkitchens.enums.RealTimeConsumeQueueStrategy;
import com.cloudkitchens.service.CourierService;
import com.cloudkitchens.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealTimeConsumeQueue implements IRealTimeConsumeQueue {

    @Autowired
    private CourierService courierService;
    @Autowired
    private KitchenService kitchenService;

    @Override
    public String getType() {
        return RealTimeConsumeQueueStrategy.REALTIME.name();
    }

    //TODO 1.快递到达队列，消费订单ready队列---实时消费（一个线程疯狂轮转）
    //重新启动一个新线程，异步执行
    @Override
    public void realTimeConsumeQueue() {

        try {

//            kitchenService.consumeOrderQueue();
//
//            kitchenService.consumeReceiveQueue();
//
//            courierService.arriveCourierConsumeReadyQueue();
//
//            courierService.delayCourierConsumeReadyQueue();

            Thread.sleep(1000);
            //TODO 异步消费 orderqueue
            new Thread(() -> {
                while (true) {
                    kitchenService.kitchenConsumeOrderQueue();
                }
            }).start();
            Thread.sleep(2000);
            //TODO 异步消费 receiveQueue
            new Thread(() -> {
                while (true) {
                    kitchenService.kitchenConsumeReceiveQueue();
                }
            }).start();
            Thread.sleep(2000);
            //TODO 异步消费 receiveQueue
            new Thread(() -> {
                while (true) {
                    courierService.arriveCourierConsumeReadyQueue();
                }
            }).start();
            Thread.sleep(2000);
            //根据系统结构设计，该队列，只有在Match策略下才有值，在FIFO策略下是没有值的
            //TODO 异步消费 receiveQueue
            new Thread(() -> {
                while (true) {
                    courierService.delayCourierConsumeReadyQueue();
                }
            }).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
