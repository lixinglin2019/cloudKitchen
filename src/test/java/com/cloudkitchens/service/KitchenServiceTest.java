package com.cloudkitchens.service;

import com.cloudkitchens.OrderDeliveryApplication;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.PropertyUtils;
import com.cloudkitchens.util.TimeUtil;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderDeliveryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KitchenServiceTest {

    @Autowired
    private KitchenService kitchenService;

    @Resource
    OrderService orderService;

    @BeforeEach
    void setUp() {
        System.out.println("1111");
    }

    @AfterEach
    void tearDown() {
        System.out.println("2222");
    }

    @Test
    void kitchenConsumeOrderQueue() {
        //MOCK 订单数据入队到order
        mockCreateOrder2OrderQueue();

        kitchenService.kitchenConsumeOrderQueue();//触发消费订单队列逻辑，下面就是参数

        System.out.println("触发");
        LinkedBlockingQueue<Order> receiveQueue = KitchenQueueEnum.KITCHEN_QUEUE.receiveQueue;

        String dispathCourierProp = PropertyUtils.getDispathCourierProp("dispathCourier.strategy");
        System.out.println("此时系统使用的dispatchCouier策略是：" + dispathCourierProp);

        System.out.println("餐厅从订单队列  orderqueue -->到receivequeue,（此时的order设置了recevieTime）");
        for (Order order : receiveQueue) {
            String orderId = order.getId();
            long kitchenReceiveTime = order.getKitchenReceiveTime();
            System.out.println("餐厅接收订单：orderid:" + orderId + " receiveTime:" + kitchenReceiveTime);

            if (DispatchCourierStrategyEnum.MATCH.name().equals(dispathCourierProp)) {
                //订单的快递当时就定好了！+ 通知快递员入队[courier-----> courierArriveQueue(快递)delayQueue)]
                Courier courier = order.getCourier();
                long courierArriveTime = order.getCourierArriveAtTime();
                long courierArriveUseTime = order.getCourierArriveUseTime();
                Assert.assertNotNull(courier);
                Assert.assertNotNull(courierArriveTime);
                Assert.assertNotNull(courierArriveUseTime);
                System.out.println("该订单安排-快递员ID：" + courier.getId());
                System.out.println("快递员到店需要耗时：" + courierArriveUseTime);
                System.out.println("快递到店时间：" + courierArriveTime);

            } else if (DispatchCourierStrategyEnum.FIFO.equals(dispathCourierProp)) {

                System.out.println("订单---没有确定快递员");
                Courier courier = order.getCourier();
                Assert.assertNull(courier);//订单---没有确定快递员

            }
        }


        System.out.println("----随后快递员员进入 优先级队列receiveQueue【优先级依据是到达时间】---");
        PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;

        for (CourierArriveDTO courierArrivePriorityDto : courierArrivedPriorityQueue) {
            System.out.println("快递到达队列中快递；" + courierArrivePriorityDto);
        }
    }


    /**
     * 制作food(消费receivey队列--->read队列)
     */
    @Test
    void kitchenConsumeReceiveQueue() {

        //MOCK 订单数据入队到order
        mockCreateOrder2OrderQueue();

        kitchenService.kitchenConsumeOrderQueue();//触发消费订单队列逻辑

        kitchenService.kitchenConsumeReceiveQueue();//制作food(消费receivey队列--->read队列)

        //检查ready队列有没有准备好数据
        DelayQueue<ReadyDTO> readyQueue = KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
//        Assert.assertTrue("餐厅准好了订单", readyQueue.size() > 0);

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                ReadyDTO take = readyQueue.take();
                Order order = take.getOrder();
                String id = order.getId();
                Integer prepTime = order.getPrepTime();
                long kitchenReceiveTime = order.getKitchenReceiveTime();
                long kitchenReadyTime = order.getKitchenReadyTime();
                String formatData = TimeUtil.getFormatData(kitchenReadyTime);
                System.out.println("订单id:"+id+" 制作完成时间："+formatData+" 餐厅接单时间："+ TimeUtil.getFormatData(kitchenReceiveTime)+" 制作需需要时间："+prepTime+" 分钟");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    private void mockCreateOrder2OrderQueue() {
        LinkedBlockingQueue<Order> orderQueue = OrderQueueEum.ORDER_QUEUE.orderQueue;
        orderQueue.clear();//先清空
//        orderService.createOrder();//创建订单，并入队
        try {
            OrderUtil.simulateConcurrentCreateOrder(orderService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("order into orderqueue:");
        for (Order order : orderQueue) {
            System.out.println(order);
        }
    }


}