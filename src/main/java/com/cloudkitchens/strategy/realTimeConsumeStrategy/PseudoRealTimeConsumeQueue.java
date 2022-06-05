package com.cloudkitchens.strategy.realTimeConsumeStrategy;

import com.cloudkitchens.enums.RealTimeConsumeQueueStrategy;
import com.cloudkitchens.service.CourierService;
import com.cloudkitchens.service.KitchenService;
import com.cloudkitchens.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class PseudoRealTimeConsumeQueue implements IRealTimeConsumeType {

    @Autowired
    private CourierService courierService;

    @Autowired
    private KitchenService kitchenService;


    @Override
    public String getType() {
        return RealTimeConsumeQueueStrategy.PSEUDO_REALTIME.name();
    }

    /**
     * 缺：效率低（伪实时、不及时）--牺牲一点点用户体验 （不过应该是值得的）
     * 优：节省cpu(不需要疯狂轮转动)---CPU 利用率，锯齿状，时高时低
     */
    ScheduledThreadPoolExecutor courierArriveConsumeReady = new ScheduledThreadPoolExecutor(5);
    //TODO 1.快递到达队列，消费订单ready队列---伪实时消费（新的线程，执行定时任务，每隔多久消费一次）
    @Override
    public void realTimeConsumeQueue() {
        int period = 1;//不配置，默认值是1
        try {
            String periodStr = PropertyUtils.getRealTimeConsumeQueueStrategyProp("pseudo_realtime.schedule.period");
            period = Integer.parseInt(periodStr);

            courierArriveConsumeReady.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        kitchenService.kitchenConsumeOrderQueue();
                        kitchenService.kitchenConsumeReceiveQueue();
                        courierService.arriveCourierConsumeReadyQueue();
                        courierService.delayCourierConsumeReadyQueue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("~~~~~~~~~~~~~~~");
                }
            }, 1, period, TimeUnit.SECONDS);//每秒执行一次
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }


    }




}
