package com.cloudkitchens.orderdelivery.event;

import com.cloudkitchens.orderdelivery.dto.KitchenDto;
import org.springframework.context.ApplicationEvent;

public class CloudKitchenMakeFinishEvent extends ApplicationEvent {


    private KitchenDto kitchenDto;

    public CloudKitchenMakeFinishEvent(Object source, KitchenDto cloudKitchen) {
        super(source);
       this.kitchenDto = cloudKitchen;
    }


    public KitchenDto getKitchenDto() {
        return kitchenDto;
    }
}
