package com.cloudkitchens.event;

import com.cloudkitchens.domain.order.Order;
import org.springframework.context.ApplicationEvent;

public class CreateOrderExceptionEvent extends ApplicationEvent {
    private String message;
    private Order order;
    public CreateOrderExceptionEvent(Object source,Order order,String message) {
        super(source);
        this.order = order;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Order getOrder() {
        return order;
    }
}
