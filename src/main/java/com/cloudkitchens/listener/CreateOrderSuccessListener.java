package com.cloudkitchens.listener;

import com.cloudkitchens.SystemStartEvent;
import com.cloudkitchens.domain.order.Order;
import com.cloudkitchens.enums.ProducerEnum;
import com.cloudkitchens.event.CreateOrderSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderSuccessListener implements ApplicationListener<CreateOrderSuccessEvent> {



    @Override
    public void onApplicationEvent(CreateOrderSuccessEvent event) {
        Order order = event.getOrder();

        //生产 订单入队
        SystemStartEvent.producerMap.get(ProducerEnum.ORDER_PRODUCER).produce(order);

    }
}
