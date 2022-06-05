package com.cloudkitchens.dto;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import lombok.Data;

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
        Long l = o1.getArriveAtTime() - this.arriveAtTime;
        return l.intValue();
    }

}
