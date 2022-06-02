package com.cloudkitchens.bridge.senMsg;

import java.util.List;

/**
 * @author lixinglin
 * @date 20:45
 */
public class MsgSenderTelephone implements MsgSender {

    private List<String> telephones;

    public MsgSenderTelephone(List<String> telephones) {
        this.telephones = telephones;
    }

    @Override
    public void sendMsg(String msg) {
        System.out.println("telephone");
        for (String telephone : telephones) {
            System.out.println("telephone" + telephone);
        }
    }
}
