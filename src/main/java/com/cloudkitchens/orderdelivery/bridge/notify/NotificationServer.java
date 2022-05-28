package com.cloudkitchens.orderdelivery.bridge.notify;


import com.cloudkitchens.orderdelivery.bridge.senMsg.MsgSender;

/**
 * @author lixinglin
 * @date 21:21
 */
public class NotificationServer extends Notification {

    //通过构造器，来实现组合关系的建立！！
    public NotificationServer(MsgSender msgSender) {
        super(msgSender);
    }

    @Override
    public void notify(String message) {
        msgSender.sendMsg(message);
    }
}
