package com.cloudkitchens.service;

import com.cloudkitchens.OrderDeliveryApplication;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderDeliveryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {
    @Resource
    OrderService orderService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrder() {
    }

    @Test
    void testCreateOrder() {
        try {
            orderService.createOrder();

            LinkedBlockingQueue<Order> orderQueue = OrderQueueEum.ORDER_QUEUE.orderQueue;
            orderQueue.clear();//先清空
            System.out.println("order into orderqueue:");

            Assert.assertNotNull(orderQueue);
            for (Order order : orderQueue) {
                System.out.println(order);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}