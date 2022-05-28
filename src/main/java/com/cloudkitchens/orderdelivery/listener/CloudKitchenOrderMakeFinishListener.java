package com.cloudkitchens.orderdelivery.listener;

import com.cloudkitchens.orderdelivery.aop.annotation.BussinessLog;
import com.cloudkitchens.orderdelivery.dao.OrderDao;
import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.dto.KitchenDto;
import com.cloudkitchens.orderdelivery.enums.OrderQueueEum;
import com.cloudkitchens.orderdelivery.enums.OrderStateEnum;
import com.cloudkitchens.orderdelivery.event.CloudKitchenMakeFinishEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.LinkedBlockingQueue;

public class CloudKitchenOrderMakeFinishListener implements ApplicationListener<CloudKitchenMakeFinishEvent> {


    @Autowired
    private OrderDao orderDao;

    /**
     * Average food wait time (milliseconds) between order ready and pickup
     * @param event
     */
    @BussinessLog("订单准备就绪")
    @Override
    public void onApplicationEvent(CloudKitchenMakeFinishEvent event) {
        KitchenDto kitchenDto = event.getKitchenDto();
        LinkedBlockingQueue makeFinishedQueue = kitchenDto.getMakeFinishedQueue();
        try {
            Order order = (Order) makeFinishedQueue.take();




            order.setState(OrderStateEnum.KITCHEN_FINISHED.state);
            orderDao.update(order);

            //全局保存 - 订单完成时间点
            OrderQueueEum.ORDER_QUEUE_EUM.getOrderMakeFinishTimeMap().put(order,System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
