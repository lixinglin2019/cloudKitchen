package com.cloudkitchens;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import com.cloudkitchens.service.OrderService;
import com.cloudkitchens.strategy.consumerQueue.IRealTimeConsumeQueue;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 系统启动后，自动触发调用
 * <p>
 * 模拟用户下单
 */
@Component
public class SystemStartEvent implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private OrderService orderService;


    static Logger log = LoggerFactory.getLogger(SystemStartEvent.class);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pintAverageWaitTimeBeforeSystemExit();
        }));
    }

    private static void pintAverageWaitTimeBeforeSystemExit() {
        System.out.println("系统退出前打印---所有订单的平均等待时间---快递员的平均等待时间");
        LinkedBlockingQueue<Order> orderQueue = OrderQueueEum.ORDER_QUEUE.orderQueue;
        int size = orderQueue.size();
        long kitchenWaitCourierTime = 0;
        long courierWaitKitchenTime = 0;
        for (Order order : orderQueue) {
            kitchenWaitCourierTime += order.getKitchenWaitCourierTime();
            courierWaitKitchenTime += order.getCourierWaitKitchenTime();
        }
        long averageKitchenWaitCourierTime = kitchenWaitCourierTime / size;
        long averageCourierWaitKitchenTime = courierWaitKitchenTime / size;
        log.info(" system has finished processing all orders, \n" +
                        "Average food wait time {}(milliseconds) between order ready and pickup\n" +
                        "Average courier wait time {}(milliseconds) between arrival and order pickup",
                averageKitchenWaitCourierTime, averageCourierWaitKitchenTime);
    }

    /**
     * 实时消费---策略
     */
    private static Map<String, IRealTimeConsumeQueue> realTimeConsumeQueueStrategyMap = new HashMap<>();
    public SystemStartEvent(List<IRealTimeConsumeQueue> consumerQueues) {
        for (IRealTimeConsumeQueue consumerQueue : consumerQueues) {
            String type = consumerQueue.getType();
            realTimeConsumeQueueStrategyMap.put(type, consumerQueue);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            try {

                //TODO 1.模拟下单
                OrderUtil.simulateConcurrentCreateOrder(orderService);

                //TODO 2.触发--实时消费
                realTimeConsume();

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void realTimeConsume() {
        String realTimeConsumeQueueStrategy = PropertyUtils.getRealTimeConsumeQueueStrategyProp("realtime.consume.queue.strategy");
        realTimeConsumeQueueStrategyMap.get(realTimeConsumeQueueStrategy).realTimeConsumeQueue();
    }


}