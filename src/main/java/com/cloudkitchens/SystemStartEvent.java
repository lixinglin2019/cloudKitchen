package com.cloudkitchens;

import com.cloudkitchens.consume.IConsume;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import com.cloudkitchens.produce.Iproduce;
import com.cloudkitchens.service.OrderService;
import com.cloudkitchens.strategy.dispatchCourier.IDispatchCourier;
import com.cloudkitchens.strategy.realTimeConsumeStrategy.IRealTimeConsumeType;
import com.cloudkitchens.strategy.realTimeConsumeStrategy.RealTimeConsumeQueue;
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
    RealTimeConsumeQueue realTimeConsumeQueue;

    @Autowired
    private OrderService orderService;




    static Logger log = LoggerFactory.getLogger(SystemStartEvent.class);
    public static Integer orderTotalNumber;
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pintAverageWaitTimeBeforeSystemExit();
        }));
        String orderTestProp = PropertyUtils.getOrderTestProp("order.totalNumber");
        orderTotalNumber = Integer.parseInt(orderTestProp);//总订单数（每秒几单）
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
    public static Map<String, IRealTimeConsumeType> realTimeConsumeQueueStrategyMap = new HashMap<>();
    public static Map<ProducerEnum, Iproduce> producerMap = new HashMap<>();
    public static Map<ConsumerEnum, IConsume> consumeMap = new HashMap<>();
    public static Map<String, IDispatchCourier> dispatchCourierStrategyMap = new HashMap<>();

    public SystemStartEvent(List<IRealTimeConsumeType> consumerQueues,
                            List<Iproduce> produceList,
                            List<IConsume> consumeList,
                            List<IDispatchCourier> dispatchCourierList) {
        for (IRealTimeConsumeType consumerQueue : consumerQueues) {
            String type = consumerQueue.getType();
            realTimeConsumeQueueStrategyMap.put(type, consumerQueue);
        }
        for (Iproduce iproduce : produceList) {
            producerMap.put(iproduce.type(), iproduce);
        }
        for (IConsume iConsume : consumeList) {
            consumeMap.put(iConsume.type(), iConsume);
        }
        for (IDispatchCourier iDispatchCourier : dispatchCourierList) {
            dispatchCourierStrategyMap.put(iDispatchCourier.getType(), iDispatchCourier);
        }
    }


    /**
     * 所有生产者
     * @param event
     */

    /**
     * 所有消费者
     *
     * @param event
     */

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            try {

                realTimeConsumeQueue.realTimeConsumeQueue();

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