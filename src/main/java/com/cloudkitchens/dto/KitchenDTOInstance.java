package com.cloudkitchens.dto;

import com.cloudkitchens.domain.KitchenInstance;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Data
public class KitchenDTOInstance {
    private int id;
    private String code;
    private String name;
    private String address;


    public static KitchenDTOInstance kitchenDto;

    //DCL
    public static KitchenDTOInstance getInstance() {
        if (kitchenDto == null) {
            synchronized (KitchenDTOInstance.class) {
                if (kitchenDto == null) {
                    KitchenInstance instance = KitchenInstance.getInstance();
                    kitchenDto = new KitchenDTOInstance();
                    BeanUtils.copyProperties(kitchenDto, instance);
                }
            }
        }
        return kitchenDto;
    }

    public static void main(String[] args) {
        String s = UUID.randomUUID().toString();
        System.out.println(s);
    }

}
