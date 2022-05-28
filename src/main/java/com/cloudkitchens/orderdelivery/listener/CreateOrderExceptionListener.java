package com.cloudkitchens.orderdelivery.listener;

import com.cloudkitchens.orderdelivery.bridge.notify.Notification;
import com.cloudkitchens.orderdelivery.bridge.notify.NotificationNormal;
import com.cloudkitchens.orderdelivery.bridge.notify.NotificationServer;
import com.cloudkitchens.orderdelivery.bridge.senMsg.MsgSenderEmail;
import com.cloudkitchens.orderdelivery.bridge.senMsg.MsgSenderTelephone;
import com.cloudkitchens.orderdelivery.dao.ExceptionOrderDao;
import com.cloudkitchens.orderdelivery.domain.order.ExceptionOrder;
import com.cloudkitchens.orderdelivery.domain.order.Order;
import com.cloudkitchens.orderdelivery.event.CreateOrderExceptionEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CreateOrderExceptionListener implements ApplicationListener<CreateOrderExceptionEvent> {

    @Autowired
    private ExceptionOrderDao exceptionOrderDao;

    @Override
    public void onApplicationEvent(CreateOrderExceptionEvent event) {

        String message = event.getMessage();//异常消息
        Order order = event.getOrder();
        //异常数据入库
        ExceptionOrder exceptionOrder = new ExceptionOrder();
        BeanUtils.copyProperties(order,exceptionOrder);
        exceptionOrder.setExceptionMessage(message);
        exceptionOrderDao.save(exceptionOrder);

        //发送告警
        sendWarn(message);
    }


    /**
     * 发送告警
     * 桥接模式
     */
    public void sendWarn(String message) {
        //电话列表 -- read from properties file
        List<String> telephones = Arrays.asList(new String[]{"15011466678", "15504252083"});
        //待发送的消息
//        String message = "可以忽略的报警";

//        MsgSenderShortMessage msgSenderShortMessage = new MsgSenderShortMessage(telephones);
        MsgSenderTelephone msgSenderShortMessage = new MsgSenderTelephone(telephones);
        Notification notificationNormal = new NotificationNormal(msgSenderShortMessage);
        notificationNormal.notify(message);


        //邮箱列表 read from properties file
        List<String> emails = Arrays.asList(new String[]{"15011466678@139.com", "7646647363@qq.com"});
        MsgSenderEmail msgSender = new MsgSenderEmail(emails);
        notificationNormal = new NotificationServer(msgSender);
        //待发送的消息
//        message = "该消息需要发送邮件报警";
        notificationNormal.notify(message);
    }
}
