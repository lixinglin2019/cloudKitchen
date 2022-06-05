package com.cloudkitchens.listener;

import com.cloudkitchens.SystemStartEvent;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.event.KitchenReceiveEvent;
import com.cloudkitchens.service.CourierService;
import com.cloudkitchens.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//餐厅接单
@Component
public class KitchenReceiveListener implements ApplicationListener<KitchenReceiveEvent> {

    @Autowired
    private CourierService courierService;


    /**
     *  1.设置订单状态
     *  2.策略模式安排快递
     */

    @Override
    public void onApplicationEvent(KitchenReceiveEvent event) {
        Order order = event.getOrder();
        if (order == null) {
            return;
        }
        order.setKitchenReceiveTime(System.currentTimeMillis());//设置餐厅 --接单时间
        /**
         * 只有餐厅接收到订单（这个时间节点）--才会安排快递员
         */

        //1.创建一个快递员 courier
        Courier courier = courierService.createCourier();



        //2.分配快递员--策略模式（最好异步）
        String dispathCourierProp = PropertyUtils.getDispathCourierProp("dispathCourier.strategy");
        SystemStartEvent.dispatchCourierStrategyMap.get(dispathCourierProp).disppatchCourier(order, courier);



    }




}
