package com.cloudkitchens.domain.user;

import lombok.Data;

import java.util.Objects;

@Data
public class Courier extends BaseUser {
    private String phone;
    private Integer DeliveryStatus;//是否被禁用(投诉中)
    private Integer score;//积分

    //id等则等
    @Override
    public boolean equals(Object o) {
        Courier courier = (Courier) o;
        String id1 = courier.getId();
        String id = this.getId();
        return Objects.equals(id, id1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phone, DeliveryStatus, score);
    }
}
