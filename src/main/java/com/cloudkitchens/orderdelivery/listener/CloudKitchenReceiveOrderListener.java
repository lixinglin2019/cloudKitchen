package com.cloudkitchens.orderdelivery.listener;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;
import com.cloudkitchens.orderdelivery.dto.KitchenDto;
import com.cloudkitchens.orderdelivery.event.CloudKitchenMakeFinishEvent;
import com.cloudkitchens.orderdelivery.event.CloudKitchenReceiveOrderEvent;
import com.cloudkitchens.orderdelivery.service.DeliveryService;
import com.cloudkitchens.orderdelivery.strategy.dispatchCourier.IDispatchCourier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

//餐厅接单
@Component
public class CloudKitchenReceiveOrderListener implements ApplicationListener<CloudKitchenReceiveOrderEvent> {


    @Autowired
    private ApplicationContext applicationContext;


    Map<String, IDispatchCourier> allDispatchStrategys = new HashMap<>();
    public CloudKitchenReceiveOrderListener(List<IDispatchCourier> dispatchCourierList) {
        for (IDispatchCourier iDispatchCourier : dispatchCourierList) {
            allDispatchStrategys.put(iDispatchCourier.getType(), iDispatchCourier);
        }
    }

    @Override
    public void onApplicationEvent(CloudKitchenReceiveOrderEvent event) {
        Order order = event.getOrder();
        KitchenDto kitchenDto = KitchenDto.getInstance();

        //订单状态修改(状态机模式)

        //TODO 调用策略模式，给此订单分配 快递员----快递员收到通知，陆续到达（入队）

        try {
            dispatchCuriorStrategy(kitchenDto);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //TODO 厨房制作 Thread.sleep(3);//制作时间从请求中取值
        Integer prepTime = order.getPrepTime();//秒
        try {
            Thread.sleep(prepTime * 1000);//制作耗时---请求中取值
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LinkedBlockingQueue makeFinishedQueue = kitchenDto.getMakeFinishedQueue();
        //制作完成，入队！
        try {
            makeFinishedQueue.put(order);

            applicationContext.publishEvent(new CloudKitchenMakeFinishEvent("", kitchenDto));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO 发送派单信息给到派送员


    }

    //异步线程池执行
    /**
     * 根据策略，为订单分配快递员
     * @param cloudKitchen
     * @throws InterruptedException
     */
    @Value("${strategy}")
    private String strategy;
    @Async
    public void dispatchCuriorStrategy(KitchenDto cloudKitchen) throws InterruptedException {
        //TODO 用策略给订单分配快递员---分配快递员
        LinkedBlockingQueue courierUsersQueue = new LinkedBlockingQueue();
        List<CourierUser> allCouriers = DeliveryService.courierUsers;//获取到快递员
        for (CourierUser courierUser : allCouriers) {
            courierUsersQueue.put(courierUser);
        }

        LinkedBlockingQueue waitForMakeOrderQueue = cloudKitchen.waitForMakeQueue;
        allDispatchStrategys.get(strategy).
                dispatchCourier(waitForMakeOrderQueue, courierUsersQueue);
        //endregion
    }
}
