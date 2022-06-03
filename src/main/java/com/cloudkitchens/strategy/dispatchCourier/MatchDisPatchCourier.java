package com.cloudkitchens.strategy.dispatchCourier;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.CourierDelayDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class MatchDisPatchCourier implements IDispatchCourier {

    Logger log = LoggerFactory.getLogger(MatchDisPatchCourier.class);

    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.MATCH.name();
    }


    /**
     * 策略A
     * 直接指定好该订单  的  快递员id
     * @param order -其实就是给一个随机的时间（3~15），然后courier入队
     *
     * 订单分配快递员得是线程安全的！否则会有一个订单分配给多个快递员的情况发生
     *
     * 1.订单分配快递员
     *
     * 2.快递员入队（优先级队列）
     *               根据快递到达耗时 作为优先级条件，
     *              * 入队courierArrivedPriorityQueue队列 快递到达队列
     *              * 耗时小的在对头部
     */

    public synchronized void disppatchCourier(Order order, Courier courier) {

        //------match体现在这里------

        // 订单绑定快递员
        order.setCourier(courier);


        //快递耗时  ---快递员的到达时间也定了
        long randomUseTime = TimeUtil.getRandomUseTime(3, 15);
        long courierArriveUseTime = randomUseTime + System.currentTimeMillis();
        order.setCourierArriveUseTime(randomUseTime);

        //快递到店时间
        long courierArriveTime = TimeUtil.courierArriveKitchenRandomTime(courierArriveUseTime);
        order.setCourierArriveTime(courierArriveTime);

        try {
            /**
             * 根据快递到达耗时 作为优先级条件，
             * 入队courierArrivedPriorityQueue队列 快递到达队列
             * 耗时小的在对头部
             */
            CourierArriveDTO courier2OrderDtoPriority = new CourierArriveDTO();
            courier2OrderDtoPriority.setOrder(order);
            courier2OrderDtoPriority.setCourier(courier);
            courier2OrderDtoPriority.setArriveUseTime(courierArriveUseTime);//作为优先级的条件，默认升序，时间小的排在前面
            PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;
            courierArrivedPriorityQueue.put(courier2OrderDtoPriority);
            System.out.println(Thread.currentThread().getName() + " 生产（快递员到达）队列:" + courier2OrderDtoPriority + ",现在快递员到达数=" + courierArrivedPriorityQueue.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 快递员pickup
     * 1.遍历到达用户（注意是遍历  不一定是按照队列出入对顺）
     * 2.每个用户去遍历 订单完成队列
     * -- 如果找到属于自己的餐，则取走，把两个队列中对应数据remove
     * -- 如果没找到，则进入延迟队列（延迟时间=根据餐厅接单时间+制作需要时间-当前时间）
     */
    @Override
    public void couriorConsumeReadyQueue() {
        DelayQueue<ReadyDTO> readyQueue = KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
        PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;
        for (CourierArriveDTO courier2OrderDtoPriority : courierArrivedPriorityQueue) {
            Courier courier = courier2OrderDtoPriority.getCourier();
            Boolean needDelay = Boolean.TRUE;
            for (ReadyDTO orderReadyDelayDto : readyQueue) {
                Order readyOrder = orderReadyDelayDto.getOrder();
                Courier courier1 = readyOrder.getCourier();
                if (courier.equals(courier1)) {
                    needDelay = Boolean.FALSE;
                    //快递取到了属于自己的订单---去送餐流程结束
                    //从队列中移除
                    arriveCourierPickupReadyOrder(readyOrder, readyQueue, courierArrivedPriorityQueue, courier2OrderDtoPriority);
                    break;
                }
            }
            if (needDelay) {//没找到自己的餐，(餐还没有制作完成)--进入延迟队列
                Order order = courier2OrderDtoPriority.getOrder();
                //正常完成时间 - 当前时间 =  还需要等待的时间
                long readyAtThisTime = order.getKitchenReceiveTime() + order.getPrepTime() * 1000;
                long needWaitTime = readyAtThisTime - System.currentTimeMillis();//还有多久完成
                CourierDelayDTO courierDelayDto = new CourierDelayDTO();
                courierDelayDto.setCourierUser(courier);
                courierDelayDto.setOrder(order);
                courierDelayDto.setNeedWaitTime(needWaitTime);
                DelayQueue<CourierDelayDTO> courierdelayQueue = CourierQueueEnum.COURIER_QUEUE.courierdelayQueue;
                courierdelayQueue.put(courierDelayDto);
                System.out.println(Thread.currentThread().getName() + " 生产（快递员延迟取餐）队列:" + courierDelayDto + ",现在(快递员延迟取餐)数=" + courierdelayQueue.size());

            }
        }
    }



    public void arriveCourierPickupReadyOrder(Order order, DelayQueue<ReadyDTO> readyQueue, PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue, CourierArriveDTO courier2OrderDtoPriority) {
        order.setOrderPickUpTime(System.currentTimeMillis());
//        courierArrivedPriorityQueue.remove(courier2OrderDtoPriority);
        OrderUtil.setTimeGap(order);
//        readyQueue.remove(order);

        OrderUtil.printTImeGap(order);
    }

    public static void main(String[] args) {
        System.out.println(8 % 8);
    }
}
