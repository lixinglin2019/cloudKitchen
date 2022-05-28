package com.cloudkitchens.orderdelivery.domain.user;

import lombok.Data;

@Data
public class CourierUser extends BaseUser{
    private String phone;
    private Integer DeliveryStatus;//是否被禁用(投诉中)
    private Integer score;//积分
}
