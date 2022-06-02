package com.cloudkitchens.event;

import com.cloudkitchens.domain.order.Order;
import org.springframework.context.ApplicationEvent;

public class CreateOrderSuccessEvent extends ApplicationEvent {
    private Order order;

    public CreateOrderSuccessEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
