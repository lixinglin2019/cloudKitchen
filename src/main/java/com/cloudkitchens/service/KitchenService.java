package com.cloudkitchens.service;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.OrderStateEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.enums.queue.OrderQueueEum;
import com.cloudkitchens.event.KitchenReceiveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class KitchenService {

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 消费订单队列数据
     * 1.从oderQueue出队、receiveQueue入队
     * 2.
     */
    public void kitchenConsumeOrderQueue() {
        /**
         * //在这里把队头元素取出并删除--同时在这里通知快递员，
         * 是考虑事务的关联性，二者有关联关系（或者说优先级，如果厨房都没有消费到消息，那么快递就算消费到也是没有意义的）
         */
        try {
            LinkedBlockingQueue orderQueue = OrderQueueEum.ORDER_QUEUE.getOrderQueue();//单例模式
            Order order = (Order) orderQueue.poll();//非阻塞等待  从订单队列取出来  --->放入 等待制作队列
            if (order == null) {//暂时没订单，则直接返回
                return;
            }
            LinkedBlockingQueue receiveQueue = KitchenQueueEnum.KITCHEN_QUEUE.getReceiveQueue();
            receiveQueue.put(order);//餐厅接单

            //同步操作（只是为了代码解耦）
            /**
             *  1.设置订单状态
             *  2.策略模式安排快递
             *
             */
            ApplicationEvent applicationEvent = new KitchenReceiveEvent("",order);
            applicationContext.publishEvent(applicationEvent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 消费餐厅接单队列，制作订单
     * 比较耗时，所以需要用@Async 异步来执行
     */


    /**
     * 默认使用springboot线程池
     * 异步制作食物
     * @Async异步方法默认使用Spring创建ThreadPoolTaskExecutor。
     * 默认核心线程数：8，
     * 最大线程数：Integet.MAX_VALUE，
     * 队列使用LinkedBlockingQueue，
     * 容量是：Integet.MAX_VALUE，
     * 空闲线程保留时间：60s，
     * 线程池拒绝策略：AbortPolicy。
     *
     * 1.异步制作食物
     * 2.做好后入队readyQueue---延时队列
     */
//    @Async
    public void kitchenConsumeReceiveQueue() {
        LinkedBlockingQueue<Order> receiveQueue = KitchenQueueEnum.KITCHEN_QUEUE.getReceiveQueue();//备好的餐
        Order receiveOrder = receiveQueue.peek();
        if (receiveOrder == null) {
            return;
        }

        //制作完成，
        // 1.修改order信息
        long kitchenReadyTime = System.currentTimeMillis() + receiveOrder.getPrepTime();//订单准备好的时间
        receiveOrder.setKitchenReadyTime(kitchenReadyTime);
        receiveOrder.setState(OrderStateEnum.KITCHEN_FINISHED.state);


        // 2.order入ready队列！
        try {
            ReadyDTO readyDTO = new ReadyDTO();
            readyDTO.setOrder(receiveOrder);
            KitchenQueueEnum.KITCHEN_QUEUE.readyQueue.put(readyDTO);

            receiveQueue.remove(receiveOrder);//把订单数据从ready队列删除
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
