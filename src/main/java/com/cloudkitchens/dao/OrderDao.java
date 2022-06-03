package com.cloudkitchens.dao;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderDao implements BaseDao<Order>{

    @Override
    public int save(Order order) {//其实就是把订单放缓存里
        Map<String, Order> orderCache = OrderQueueEum.ORDER_QUEUE.orderCache;
        boolean existOrder = orderCache.containsKey(order.getId());
        if (existOrder){
            return 0;
        }else {
            orderCache.putIfAbsent(order.getId(),order);
        }
        return 0;
    }

    @Override
    public int delete(Order object) {
        return 0;
    }

    @Override
    public int update(Order object) {
        return 0;
    }

    @Override
    public Order findById(String id) {
        return null;
    }
}
