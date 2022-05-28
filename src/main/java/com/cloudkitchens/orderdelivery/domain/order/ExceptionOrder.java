package com.cloudkitchens.orderdelivery.domain.order;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import lombok.Data;

@Data
public class ExceptionOrder extends Order {
    private String exceptionMessage;
}
