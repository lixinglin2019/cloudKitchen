package com.cloudkitchens.orderdelivery.bridge.senMsg;

import java.util.List;

/**
 * @author lixinglin
 * @date 09:50
 */
public class MsgSenderShortMessage implements MsgSender {
    private List<String> telephones;

    public MsgSenderShortMessage(List<String> userList) {
        this.telephones = userList;
    }


    public void sendMsg(String message) {
        for (String s : telephones) {
            System.out.println("发短信给:"+s+"   内容是："+message);
        }
    }
}
