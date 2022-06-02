package com.cloudkitchens.strategy.dispatchCourier;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class FIFODispatchCourier implements IDispatchCourier {

    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.FIFO.name();
    }

    /**
     * 策略B
     * 不指点该订单的 快递号
     * 而是延后到 快递员取到餐 才能确定到底是哪个外卖员来取餐
     *  根据快递到达耗时 作为优先级条件，
     *              * 入队courierArrivedPriorityQueue队列 快递到达队列
     *              * 耗时小的在对头部
     */


    @Override
    public void disppatchCourier(Order order, Courier courier) {
        //快递耗时
        long randomUseTime = TimeUtil.getRandomUseTime(3, 15);
        long courierArriveUseTime = randomUseTime + System.currentTimeMillis();
        try {

            CourierArriveDTO courier2OrderDtoPriority = new CourierArriveDTO();
//            courier2OrderDtoPriority.setOrder(order); 不确定订单-到现场随机取
            courier2OrderDtoPriority.setCourier(courier);
            courier2OrderDtoPriority.setArriveUseTime(courierArriveUseTime);
            courier2OrderDtoPriority.setArriveAtTime(courierArriveUseTime+System.currentTimeMillis());

            CourierQueueEnum.COURIER_QUEUE.getCourierArrivedPriorityQueue().add(courier2OrderDtoPriority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 快递员pickup
     * 这个策略就需要队列的FIFO特性
     * 结束队列api来完成
     *
     * 快递员会阻塞等待取餐，
     * 队头的取不到，别的也就的都排好队等着
     */
    @Override
    public void couriorConsumeReadyQueue() {
        DelayQueue<ReadyDTO> readyQueue = KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
        PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = CourierQueueEnum.COURIER_QUEUE.getCourierArrivedPriorityQueue();
        CourierArriveDTO courier2OrderPriorityDto = courierArrivedPriorityQueue.peek();
        if (courier2OrderPriorityDto == null) {//快递员队列 没有人
            return;
        }
        ReadyDTO peek = readyQueue.peek();
        if (peek == null) {//餐厅没有准备好的订单
            return;
        }
        Courier courier = courier2OrderPriorityDto.getCourier();
        Long arriveAtTime = courier2OrderPriorityDto.getArriveAtTime();
        Long arriveUseTime = courier2OrderPriorityDto.getArriveUseTime();

        Order readOrder = peek.getOrder();
        readOrder.setCourier(courier);//这里才确定该订单被哪个快递员拿到了
        readOrder.setCourierArriveTime(arriveAtTime);
        readOrder.setCourierArriveUseTime(arriveUseTime);
        readyPickup(readOrder,readyQueue, courierArrivedPriorityQueue, courier2OrderPriorityDto);
    }
    Logger log = LoggerFactory.getLogger(FIFODispatchCourier.class);

    public void readyPickup(Order order, DelayQueue<ReadyDTO> readyQueue, PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue, CourierArriveDTO courier2OrderDtoPriority) {
        order.setOrderPickUpTime(System.currentTimeMillis());
        courierArrivedPriorityQueue.remove(courier2OrderDtoPriority);

        OrderUtil.setTimeGap(order);

        readyQueue.remove(order);

        OrderUtil.printTImeGap(order);
    }

}
