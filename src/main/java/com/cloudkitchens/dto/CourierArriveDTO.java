package com.cloudkitchens.dto;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.util.TimeUtil;
import lombok.Data;

import java.util.concurrent.PriorityBlockingQueue;

//快递（订单）+到达时间 中间装换对象
@Data
public class CourierArriveDTO implements Comparable {
    private Courier courier;
    private Order order;//该值，可有可没有，如果是策略1.Math 则有值  如果是策略2.FIFO 则还没定下来该courier取哪个订单，需要去了餐厅线程才能定
    private Long arriveAtTime;
    private Long arriveUseTime;


    @Override
    public int compareTo(Object o) {
        CourierArriveDTO o1 = (CourierArriveDTO) o;
        Long l = this.arriveAtTime - o1.getArriveAtTime();//升序（对象属性在前-就是升序）
        return l.intValue();
    }

    public static void main(String[] args) {
        int couiorNumber = 7;
        PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue(4);
        for (int i = 0; i < couiorNumber; i++) {
            CourierArriveDTO courierArriveDTO = new CourierArriveDTO();
            courierArriveDTO.setArriveAtTime(System.currentTimeMillis() + i * 1000);
            priorityBlockingQueue.put(courierArriveDTO);
        }

        System.out.println("先到达的排前面");
        while (true) {//这里完全可以生效--只是多线程消费需要记得线程安全问题
            couiorNumber--;
            CourierArriveDTO take = null;
            try {
                take = (CourierArriveDTO) priorityBlockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Long arriveAtTime = take.getArriveAtTime();
            String formatData = TimeUtil.getFormatData(arriveAtTime);
            System.out.println(formatData);
        }
    }

}
