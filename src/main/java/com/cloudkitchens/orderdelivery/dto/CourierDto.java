package com.cloudkitchens.orderdelivery.dto;

import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.domain.user.CourierUser;
import lombok.Data;

import java.util.List;

@Data
public class CourierDto {
    private CourierUser courierUser;
    private List<Order> orderList;
    private Long arriveTime;
}
