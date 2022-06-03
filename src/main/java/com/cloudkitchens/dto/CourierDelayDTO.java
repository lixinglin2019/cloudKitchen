package com.cloudkitchens.dto;

import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

//快递（订单）+到达时间 中间装换对象
@Data
public class CourierDelayDTO implements Delayed {
    private Courier courierUser;
    private Order order;//该值，可有可没有，如果是策略1.Math 则有值  如果是策略2.FIFO 则还没定下来该courier取哪个订单，需要去了餐厅线程才能定

    long needWaitTime; // 延迟时间

    //决定延迟多久后执行---多久之后可以从队列中取出来
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.needWaitTime, TimeUnit.MILLISECONDS);
    }

    //决定排队顺序-----排队先后顺序
    @Override
    public int compareTo(Delayed o) {
        long f = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return (int) f;
    }
}
