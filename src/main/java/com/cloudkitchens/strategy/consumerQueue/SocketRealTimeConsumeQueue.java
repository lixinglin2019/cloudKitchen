package com.cloudkitchens.strategy.consumerQueue;

import com.cloudkitchens.enums.RealTimeConsumeQueueStrategy;
import org.springframework.stereotype.Component;

@Component
public class SocketRealTimeConsumeQueue implements IRealTimeConsumeQueue {
    @Override
    public String getType() {
        return RealTimeConsumeQueueStrategy.SOCKET_REALTIME.name();
    }

    //TODO 1.快递到达队列，消费订单ready队列---真实时消费（底层socket通讯机制-利用长连接）
    @Override
    public void realTimeConsumeQueue() {
        throw new UnsupportedOperationException("not support");
    }
}
