package com.cloudkitchens.service;

import com.cloudkitchens.OrderDeliveryApplication;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.CourierDelayDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import com.cloudkitchens.util.PropertyUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderDeliveryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourierServiceTest {

    @Autowired
    private CourierService courierService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private KitchenService kitchenService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void arriveCourierConsumeReadyQueue() {
        //MOCK 订单数据入队到order
        mockCreateOrder2OrderQueue();

        kitchenService.kitchenConsumeOrderQueue();//触发消费订单队列逻辑

        kitchenService.kitchenConsumeReceiveQueue();//制作food(消费receivey队列--->read队列)

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //延迟取餐快递
        DelayQueue<CourierDelayDTO> courierdelayQueue = CourierQueueEnum.COURIER_QUEUE.courierdelayQueue;

        //到达的快递
        PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;

        //准备好的餐
        DelayQueue<ReadyDTO> readyQueue = KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;

        for (ReadyDTO readyDTO : readyQueue) {
            System.out.println("已经准备好的餐:" + readyDTO);
        }
        for (CourierArriveDTO courierArriveDTO : courierArrivedPriorityQueue) {
            System.out.println("已经到达的快递：" + courierArriveDTO);
        }

        System.out.println("----------开始pickup------------");

        courierService.arriveCourierConsumeReadyQueue();

        System.out.println("取餐后结果");

        for (ReadyDTO readyDTO : readyQueue) {
            System.out.println("已经准备好的餐:" + readyDTO);
        }

        for (CourierArriveDTO courierArriveDTO : courierArrivedPriorityQueue) {
            System.out.println("已经到达的快递：" + courierArriveDTO);
        }


        String dispathCourierProp = PropertyUtils.getDispathCourierProp("dispathCourier.strategy");
        System.out.println("此时系统使用的dispatchCouier策略是：" + dispathCourierProp);
        if (DispatchCourierStrategyEnum.MATCH.name().equals(dispathCourierProp)) {
            //遍历队列--做消费readyQueue

            //没有找到订单的入队CourierDelayQueue
            System.out.println("Math 非消费，没有取到，则快递入队-delay队列");

            for (CourierDelayDTO courierDelayDTO : courierdelayQueue) {
                //如果策略是MATCH --则可能有延时队列
                System.out.println("delay取餐的快递：" + courierDelayDTO);
            }
        }
        if (DispatchCourierStrategyEnum.FIFO.equals(dispathCourierProp)) {

            System.out.println("FIFO 阻塞消费");
            //基于FIFO 阻塞消费--没有延迟队列一说（就是死等）
        }

    }

    @Test
    void delayCourierConsumeReadyQueue() {

        String dispathCourierProp = PropertyUtils.getDispathCourierProp("dispathCourier.strategy");
        System.out.println("此时系统使用的dispatchCouier策略是：" + dispathCourierProp);
        if (DispatchCourierStrategyEnum.MATCH.name().equals(dispathCourierProp)) {
            //遍历队列--做消费readyQueue

            //没有找到订单的入队CourierDelayQueue
            System.out.println("Math 非消费，没有取到，则快递入队-delay队列");
            //MOCK 订单数据入队到order
            mockCreateOrder2OrderQueue();

            kitchenService.kitchenConsumeOrderQueue();//触发消费订单队列逻辑

            kitchenService.kitchenConsumeReceiveQueue();//制作food(消费receivey队列--->read队列)

            DelayQueue<CourierDelayDTO> courierdelayQueue = CourierQueueEnum.COURIER_QUEUE.courierdelayQueue;

            for (CourierDelayDTO courierDelayDTO : courierdelayQueue) {
                //如果策略是MATCH --则可能有延时队列
                System.out.println("delay取餐的快递：" + courierDelayDTO);
            }
            courierService.delayCourierConsumeReadyQueue();


        }
        if (DispatchCourierStrategyEnum.FIFO.equals(dispathCourierProp)) {

            System.out.println("FIFO 阻塞消费---该策略模式 不需要delayqueue");
            return;//该策略模式 不需要delayqueue
        }

    }


    @Test
    void createCourier() {
        Courier courier = courierService.createCourier();
        System.out.println("快递被创建：" + courier.toString());
        List<Courier> allCouriers = CourierQueueEnum.COURIER_QUEUE.allCouriers;
        Assert.assertTrue(allCouriers.size() > 0);
        for (Courier courier1 : allCouriers) {
            System.out.println(" 快递被放入内存（Map）中：" + courier1);
        }
    }


    private void mockCreateOrder2OrderQueue() {
        LinkedBlockingQueue<Order> orderQueue = OrderQueueEum.ORDER_QUEUE.orderQueue;
        orderQueue.clear();//先清空
        orderService.createOrder();//创建订单，并入队

        System.out.println("order into orderqueue:");
        for (Order order : orderQueue) {
            System.out.println(order);
        }
    }
}