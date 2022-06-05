package com.cloudkitchens.strategy.dispatchCourier;

import com.cloudkitchens.SystemStartEvent;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.ConsumerEnum;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

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
     * 根据快递到达耗时 作为优先级条件，
     * * 入队courierArrivedPriorityQueue队列 快递到达队列
     * * 耗时小的在对头部
     */


    @Override
    public void disppatchCourier(Order order, Courier courier) {

        /**
         * 准备数据
         */
        //快递耗时
        long randomUseTime = TimeUtil.getRandomUseTime(3, 15);
        long courierArriveAtTime = randomUseTime + System.currentTimeMillis();
        CourierArriveDTO courier2OrderDtoPriority = new CourierArriveDTO();
//      courier2OrderDtoPriority.setOrder(order); 不确定订单-到现场随机取
        courier2OrderDtoPriority.setCourier(courier);
        courier2OrderDtoPriority.setArriveUseTime(randomUseTime);
        courier2OrderDtoPriority.setArriveAtTime(courierArriveAtTime);

        //生产
        SystemStartEvent.producerMap.get(ProducerEnum.COURIER_ARRIVED_PRODUCER).produce(courier2OrderDtoPriority);

    }

    /**
     * 快递员pickup
     * 这个策略就需要队列的FIFO特性
     * 结束队列api来完成
     * <p>
     * 快递员会阻塞等待取餐，
     * 队头的取不到，别的也就的都排好队等着
     */
    @Override
    public void couriorConsumeReadyQueue() {
        AtomicInteger courierPickUpOrderNumer = CourierQueueEnum.COURIER_QUEUE.courierPickUpOrderNumer;
        new Thread(() -> {
            while (courierPickUpOrderNumer.get() < SystemStartEvent.orderTotalNumber) {
                //消费
                Object courierDTO = SystemStartEvent.consumeMap.get(ConsumerEnum.COURIER_ARRIVED_CONSUMER).consume();//优先级队列
                if (courierDTO == null) {
                    return;
                }

                //消费
                Object consume = SystemStartEvent.consumeMap.get(ConsumerEnum.KITCHEN_READY_CONSUMER).consume();//延迟队列
                if (consume == null) {
                    return;
                }


                CourierArriveDTO courierArriveDTO = (CourierArriveDTO) courierDTO;
                Courier courier = courierArriveDTO.getCourier();
                Long arriveAtTime = courierArriveDTO.getArriveAtTime();
                Long arriveUseTime = courierArriveDTO.getArriveUseTime();

                ReadyDTO readyDTO = (ReadyDTO) consume;
                Order readOrder = readyDTO.getOrder();
                readOrder.setCourier(courier);//这里才确定该订单被哪个快递员拿到了
                readOrder.setCourierArriveAtTime(arriveAtTime);
                readOrder.setCourierArriveUseTime(arriveUseTime);
                readOrder.setOrderPickUpTime(System.currentTimeMillis());

                OrderUtil.setTimeGap(readOrder);
                OrderUtil.printTimegap(readOrder);

                int andIncrement = courierPickUpOrderNumer.getAndIncrement();
                if (andIncrement == SystemStartEvent.orderTotalNumber-1) {
                    OrderUtil.pintOrderCache();
                }
            }
        }).start();

    }

}
