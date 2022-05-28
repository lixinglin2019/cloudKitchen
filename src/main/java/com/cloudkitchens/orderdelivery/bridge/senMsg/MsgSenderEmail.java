package com.cloudkitchens.orderdelivery.bridge.senMsg;

import java.util.List;

/**
 * @author lixinglin
 * @date 20:46
 */
public class MsgSenderEmail implements MsgSender {
    private List<String> emails;

    public MsgSenderEmail(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public void sendMsg(String msg) {
        for (String email : emails) {
            System.out.println("发送邮件给email:" + email + "内容是：" + msg);
        }
    }
}
