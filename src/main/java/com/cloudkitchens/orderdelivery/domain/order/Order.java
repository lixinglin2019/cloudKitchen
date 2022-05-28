package com.cloudkitchens.orderdelivery.domain.order;

import com.cloudkitchens.orderdelivery.domain.BaseEntity;
import com.cloudkitchens.orderdelivery.domain.Kitchen;
import com.cloudkitchens.orderdelivery.enums.OrderStateEnum;
import lombok.Data;

@Data
public class Order extends BaseEntity {
    private String name;
    private Integer prepTime;//单位：秒
    private Kitchen kitchen = Kitchen.getInstance();//单例模式，订单默认下到该餐厅

    private String courierId;
    private Integer state = OrderStateEnum.OPEN.state;


}

