package com.cloudkitchens.orderdelivery.dto;

import com.cloudkitchens.orderdelivery.domain.Kitchen;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class KitchenDto {
    private int id;
    private String code;
    private String name;
    private String address;

    //每个餐厅都有两个队列，1个是保存接收的订单 1个是保存已经处理好的订单
    //餐厅承接订单队列
    public LinkedBlockingQueue waitForMakeQueue = new LinkedBlockingQueue();

    //餐厅制作完成队列
    public LinkedBlockingQueue makeFinishedQueue = new LinkedBlockingQueue();

    public static KitchenDto kitchenDto;

    //DCL
    public static KitchenDto getInstance() {
        if (kitchenDto == null) {
            synchronized (KitchenDto.class) {
                if (kitchenDto == null) {
                    Kitchen instance = Kitchen.getInstance();
                    kitchenDto = new KitchenDto();
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
