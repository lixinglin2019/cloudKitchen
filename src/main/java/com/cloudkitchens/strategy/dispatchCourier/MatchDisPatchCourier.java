package com.cloudkitchens.strategy.dispatchCourier;

import com.cloudkitchens.SystemStartEvent;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.dto.CourierArriveDTO;
import com.cloudkitchens.dto.CourierDelayDTO;
import com.cloudkitchens.dto.ReadyDTO;
import com.cloudkitchens.enums.DispatchCourierStrategyEnum;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.enums.queue.CourierQueueEnum;
import com.cloudkitchens.enums.queue.KitchenQueueEnum;
import com.cloudkitchens.util.OrderUtil;
import com.cloudkitchens.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class MatchDisPatchCourier implements IDispatchCourier {
    /**
     * 快递员pickup
     * 1.遍历到达用户（注意是遍历  不一定是按照队列出入对顺）
     * 2.每个用户去遍历 订单完成队列
     * -- 如果找到属于自己的餐，则取走，把两个队列中对应数据remove
     * -- 如果没找到，则进入延迟队列（延迟时间=根据餐厅接单时间+制作需要时间-当前时间）
     */
    @Override
    public void couriorConsumeReadyQueue() {
        new Thread(() -> {


            while (CourierQueueEnum.COURIER_QUEUE.courierPickUpOrderNumer.get() <SystemStartEvent.orderTotalNumber) {
                //消费-已到达的快递员
                PriorityBlockingQueue<CourierArriveDTO> courierArrivedPriorityQueue = CourierQueueEnum.COURIER_QUEUE.courierArrivedPriorityQueue;
                Iterator<CourierArriveDTO> courierArriveDTOIterator = courierArrivedPriorityQueue.iterator();
                while (courierArriveDTOIterator.hasNext()) {
                    CourierArriveDTO courierArriveDTO = courierArriveDTOIterator.next();//MATCH策略，订单和快递是已经绑定了的，可以在里面确定二者关系
                    Courier courier = courierArriveDTO.getCourier();//-------快递员
                    Order order = courierArriveDTO.getOrder();//----订单
                    //            遍历已经准备好的餐，找到自己的
                    DelayQueue<ReadyDTO> readyQueue = KitchenQueueEnum.KITCHEN_QUEUE.readyQueue;
                    Iterator<ReadyDTO> readyDTOIterator = readyQueue.iterator();
                    Boolean needDelay = Boolean.TRUE;
                    while (readyDTOIterator.hasNext()) {
                        ReadyDTO readyDTO = readyDTOIterator.next();
                        Order order1 = readyDTO.getOrder();
                        Courier courier1 = order1.getCourier();
                        boolean equals = courier1.equals(courier);
                        if (equals) {
                            needDelay = Boolean.FALSE;
                            //pickup 取走，结束
                            readyDTOIterator.remove();//从准备好队列中移走
                            courierArriveDTOIterator.remove();//从准备好队列中移走

                            order.setOrderPickUpTime(System.currentTimeMillis());  //pickup
                            OrderUtil.setTimeGap(order);
                            OrderUtil.printTimegap(order);

                            CourierQueueEnum.COURIER_QUEUE.courierPickUpOrderNumer.getAndIncrement();
                        }
                    }
                    if (needDelay){//进入快递员delay队列
                        /**
                         * 准备数据
                         */
                        //正常完成时间 - 当前时间 =  还需要等待的时间
                        long readyAtThisTime = order.getKitchenReceiveTime() + order.getPrepTime() * 1000;
                        long needWaitTime = readyAtThisTime - System.currentTimeMillis();//还有多久完成
                        CourierDelayDTO courierDelayDto = new CourierDelayDTO();
                        courierDelayDto.setCourierUser(courier);
                        courierDelayDto.setOrder(order);
                        courierDelayDto.setNeedWaitTime(needWaitTime);
                        //生产
                        SystemStartEvent.producerMap.get(ProducerEnum.COURIER_DELAY_PRODUCER).produce(courierDelayDto);
                    }
                }
            }
        }).start();

    }

    @Override
    public String getType() {
        return DispatchCourierStrategyEnum.MATCH.name();
    }

    /**
     * 策略A
     * 直接指定好该订单  的  快递员id
     */

    @Override
    public synchronized void disppatchCourier(Order order, Courier courier) {

        // 订单绑定快递员
        Courier courier1 = order.getCourier();//校验：为了线程安全
        if (courier1 != null) {
            return;
        }
        long randomUseTime = TimeUtil.getRandomUseTime(3, 15);//毫秒
        long currentTimeMillis = System.currentTimeMillis();


        long courierArriveAtTime = randomUseTime + currentTimeMillis;


        String formatData = TimeUtil.getFormatData(currentTimeMillis);
        System.out.println("当前时间：" + formatData);
        System.out.println("快递路上花费时间（毫秒)：" + randomUseTime);
        String formatData1 = TimeUtil.getFormatData(courierArriveAtTime);
        System.out.println("快递应该到达时间：" + formatData1);


        order.setCourier(courier);  //------match体现在这里------
        order.setCourierArriveUseTime(randomUseTime);//快递耗时
        order.setCourierArriveAtTime(courierArriveAtTime);//快递到店时间


        /**
         * 根据快递到达耗时 作为优先级条件，
         * 入队courierArrivedPriorityQueue队列 快递到达队列
         * 耗时小的在对头部
         */
        CourierArriveDTO courier2OrderDtoPriority = new CourierArriveDTO();
        courier2OrderDtoPriority.setOrder(order);
        courier2OrderDtoPriority.setCourier(courier);
        courier2OrderDtoPriority.setArriveUseTime(randomUseTime);//作为优先级的条件，默认升序，时间小的排在前面
        courier2OrderDtoPriority.setArriveAtTime(courierArriveAtTime);

        //生产
        SystemStartEvent.producerMap.get(ProducerEnum.COURIER_ARRIVED_PRODUCER).produce(courier2OrderDtoPriority);


    }


    /*public static void main(String[] args) {
        System.out.println(8 % 8);

        long randomUseTime = TimeUtil.getRandomUseTime(3, 15);//毫秒
        long now = System.currentTimeMillis();
        long courierArriveUseTime = randomUseTime + now;
        System.out.println(now);
        String formatData1 = TimeUtil.getFormatData(now);
        System.out.println("now:" + formatData1);
        System.out.println(courierArriveUseTime);
        String formatData = TimeUtil.getFormatData(courierArriveUseTime);
        System.out.println("arrive:" + formatData);

    }*/
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Message> queue = new DelayQueue<>();
        long now = System.currentTimeMillis();
        queue.add(new Message(now + 5000));
        queue.add(new Message(now + 8000));
        queue.add(new Message(now + 2000));
        queue.add(new Message(now + 1000));
        queue.add(new Message(now + 7000));


        Iterator<Message> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            long x = message.deadline - now;
            if (x == 7000) {
                iterator.remove();
            }
        }


//        new Thread(() -> {
        while (true) {
            try {
                // 1000.2000.5000。7000.8000
                System.out.println("~~~~~" + (queue.take().deadline - now));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        }).start();
//        Thread.sleep(1000);
//        System.out.println("--------------");


    }

    static class Message implements Delayed {
        long deadline;

        public Message(long deadline) {
            this.deadline = deadline;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return deadline - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return String.valueOf(deadline);
        }
    }
}
