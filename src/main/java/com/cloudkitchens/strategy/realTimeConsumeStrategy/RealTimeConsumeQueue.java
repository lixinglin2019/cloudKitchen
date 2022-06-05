package com.cloudkitchens.strategy.realTimeConsumeStrategy;

import com.cloudkitchens.enums.RealTimeConsumeQueueStrategy;
import com.cloudkitchens.service.CourierService;
import com.cloudkitchens.service.KitchenService;
import com.cloudkitchens.service.OrderService;
import com.cloudkitchens.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealTimeConsumeQueue implements IRealTimeConsumeType {

    @Autowired
    private OrderService orderService;
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

            //生产 1.模拟下单
            OrderUtil.simulateConcurrentCreateOrder(orderService);



            Thread.sleep(1000);
            //TODO 新线程异步消费 orderqueue
            kitchenService.kitchenConsumeOrderQueue();


            Thread.sleep(1000);
            //TODO 新线程异步消费 receiveQueue
            kitchenService.kitchenConsumeReceiveQueue();


            Thread.sleep(1000);
            //TODO 新线程异步消费 receiveQueue
            courierService.arriveCourierConsumeReadyQueue();


            Thread.sleep(1000);
            //根据系统结构设计，该队列，只有在Match策略下才有值，在FIFO策略下是没有值的
            //TODO 新线程异步消费 receiveQueue
            courierService.delayCourierConsumeReadyQueue();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
