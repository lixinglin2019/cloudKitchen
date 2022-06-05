package com.cloudkitchens.service;

import com.cloudkitchens.SystemStartEvent;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.OrderStateEnum;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.event.KitchenReceiveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KitchenService {

    @Autowired
    private ApplicationContext applicationContext;
//    private volatile AtomicInteger kitchenReceiveOrderNumer = KitchenQueueEnum.KITCHEN_QUEUE.kitchenReceiveOrderNumber;

    /**
     * 消费订单队列数据
     * 1.从oderQueue出队、receiveQueue入队
     * 2.
     */
    public void kitchenConsumeOrderQueue() {
        AtomicInteger kitchenReceiveOrderNumber = KitchenQueueEnum.KITCHEN_QUEUE.kitchenReceiveOrderNumber;
        new Thread(() -> {
            while (kitchenReceiveOrderNumber.get() < SystemStartEvent.orderTotalNumber) {
                try {
                    //消费
                    Object consume = SystemStartEvent.consumeMap.get(ConsumerEnum.ORDER_CONSUMER).consume();
                    if (consume == null) {
                        return;
                    }
                    Order order = (Order) consume;

                    //生产
//                    SystemStartEvent.producerMap.get(ProducerEnum.KITCHEN_RECEIVE_PRODUCER).produce(order);
                    SystemStartEvent.producerMap.get(ProducerEnum.KITCHEN_RECEIVE_PRODUCER).produce(order);
                    //计数器+
//                    看看这样能否写上？
                    kitchenReceiveOrderNumber.getAndIncrement();


                    //同步操作（只是为了代码解耦）
                    ApplicationEvent applicationEvent = new KitchenReceiveEvent("", order);
                    applicationContext.publishEvent(applicationEvent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /**
             * //在这里把队头元素取出并删除--同时在这里通知快递员，
             * 是考虑事务的关联性，二者有关联关系（或者说优先级，如果厨房都没有消费到消息，那么快递就算消费到也是没有意义的）
             */
        }).start();

    }

    /**
     * 消费餐厅接单队列，制作订单
     * 比较耗时，所以需要用@Async 异步来执行
     */


    /**
     * 默认使用springboot线程池
     * 异步制作食物
     *
     * @Async异步方法默认使用Spring创建ThreadPoolTaskExecutor。 默认核心线程数：8，
     * 最大线程数：Integet.MAX_VALUE，
     * 队列使用LinkedBlockingQueue，
     * 容量是：Integet.MAX_VALUE，
     * 空闲线程保留时间：60s，
     * 线程池拒绝策略：AbortPolicy。
     * <p>
     * 1.异步制作食物
     * 2.做好后入队readyQueue---延时队列
     */
    public void kitchenConsumeReceiveQueue() {
        AtomicInteger kitchenReceiveReadyOrderNumber = KitchenQueueEnum.KITCHEN_QUEUE.kitchenReceiveReadyOrderNumber;
        new Thread(() -> {
            while (kitchenReceiveReadyOrderNumber.get() < SystemStartEvent.orderTotalNumber) {
                //消费
                Object consume = SystemStartEvent.consumeMap.get(ConsumerEnum.KITCHEN_RECEIVE_CONSUMER).consume();
                if (consume == null) {
                    return;
                }

                /**
                 * 准备数据
                 */
                Order order = (Order) consume;
                long kitchenReadyTime = System.currentTimeMillis() + order.getPrepTime();//订单准备好的时间
                order.setKitchenReadyTime(kitchenReadyTime);
                order.setState(OrderStateEnum.KITCHEN_FINISHED.state);
                ReadyDTO readyDTO = new ReadyDTO();
                readyDTO.setOrder(order);

                //生产
                SystemStartEvent.producerMap.get(ProducerEnum.KITCHEN_READY_PRODUCER).produce(readyDTO);
                kitchenReceiveReadyOrderNumber.getAndIncrement();//i++
            }
        }).start();


    }


}
