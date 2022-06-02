package com.cloudkitchens.bridge.notify;


import com.cloudkitchens.bridge.senMsg.MsgSender;

/**
 * @author lixinglin
 * @date 20:49
 */
public class NotificationNormal extends Notification {


    public NotificationNormal(MsgSender msgSender) {
        super(msgSender);
    }

    @Override
    public void notify(String message) {
        msgSender.sendMsg(message);

    }
}
