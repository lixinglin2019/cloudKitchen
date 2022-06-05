package com.cloudkitchens.domain.order;

import com.cloudkitchens.domain.BaseEntity;
import com.cloudkitchens.domain.KitchenInstance;
import com.cloudkitchens.domain.user.Courier;
import com.cloudkitchens.enums.OrderStateEnum;
import com.cloudkitchens.util.TimeUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class Order extends BaseEntity {

    Logger log = LoggerFactory.getLogger(Order.class);
    private String name;
    private Integer prepTime;//单位：秒
    private KitchenInstance kitchen = KitchenInstance.getInstance();//单例模式，订单默认下到该餐厅


    private Courier courier;//这里由策略来决定-何时设置
    //为了方便统计和打印，把时间都保存下来
    private long kitchenReceiveTime = 0;//餐厅接收时间
    private long kitchenReadyTime = 0;//餐厅制作完成时间
    private long courierArriveUseTime = 0;//3~15 秒 快递到店路上耗时
    private long courierArriveAtTime = 0;//快递到店物理时间
    private long orderPickUpTime = 0;//订单被pickup时间
    /**
     * --耗时点在快递员
     * 餐厅等待快递时长（单位毫秒）
     * 如果courierArriveTime>kitchenReadyTime
     * 设置此值=courierArriveTime-kitchenReadyTime
     */
    private long kitchenWaitCourierTime;
    /**
     * ---耗时点在餐厅
     * 快递等待餐厅时长（单位毫秒）
     * 如果kitchenReadyTime > courierArriveTime
     * 设置此值=kitchenReadyTime-courierArriveTime
     */
    private long courierWaitKitchenTime;

    private Integer state = OrderStateEnum.OPEN.state;

    public void setCourier(Courier courier) {

        printLog("courier dispatched",System.currentTimeMillis());
        this.courier = courier;
    }

    public void setKitchenReceiveTime(long kitchenReceiveTime) {
        printLog("order received    ",kitchenReceiveTime);
        this.kitchenReceiveTime = kitchenReceiveTime;
    }
    public void setKitchenReadyTime(long kitchenReadyTime) {
        printLog("order ready       ",kitchenReadyTime);
        this.kitchenReadyTime = kitchenReadyTime;
    }
    public void setCourierArriveUseTime(long courierArriveUseTime) {
        log.info("orderId:{} {} {}(milliseconds) ",this.getId(), "courier arrive use time", courierArriveUseTime);
        this.courierArriveUseTime = courierArriveUseTime;
    }
    public void setCourierArriveAtTime(long courierArriveAtTime) {
        printLog("courier arrived   ",courierArriveAtTime);
        this.courierArriveAtTime = courierArriveAtTime;
    }

    public void setOrderPickUpTime(long orderPickUpTime) {
        printLog("order picked up   ",orderPickUpTime);
        this.orderPickUpTime = orderPickUpTime;
    }

    public void printLog(String event,long time){
        String formatData = TimeUtil.getFormatData(time);
        log.info("orderId:{} {}   at {} ",this.getId(), event, formatData);
    }
}

