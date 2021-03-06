package com.cloudkitchens.bridge.notify;


import com.cloudkitchens.bridge.senMsg.MsgSender;

/**
 * @author lixinglin
 * @date 20:43
 */
public abstract class Notification {

    protected MsgSender msgSender;

    public Notification(MsgSender msgSender) {
        this.msgSender = msgSender;
    }

    public abstract void notify(String message);
}
