package com.cloudkitchens.dto;

import com.cloudkitchens.domain.order.Order;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

//快递（订单）+到达时间 中间装换对象
@Data
public class ReadyDTO implements Delayed {

    private Order order;//该值，可有可没有，如果是策略1.Math 则有值  如果是策略2.FIFO 则还没定下来该courier取哪个订单，需要去了餐厅线程才能定

    //订单的接收时间点+制作时间---这就是最终订单ready好的时间点（他作为delay的时间依据）
    @Override
    public long getDelay(TimeUnit unit) {
        int prepTime = order.getPrepTime() * 1000;
        long kitchenReceiveTime = order.getKitchenReceiveTime();
        long sourceDuration = prepTime + kitchenReceiveTime;
        return unit.convert(sourceDuration, TimeUnit.MILLISECONDS);//从现在开始时间点开始+准备消耗时间=准备结束的实际点
    }

    @Override
    public int compareTo(Delayed o) {
        long f = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return (int) f;
    }
}
