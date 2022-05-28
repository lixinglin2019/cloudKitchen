package com.cloudkitchens.orderdelivery;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.UUID;

class OrderControllerTest {
    @Resource
    private OrderService orderService;
    Order order  ;
    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setName("egg");
        order.setPrepTime(20);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrder() throws Exception {
        orderService.createOrder(order);
    }
}