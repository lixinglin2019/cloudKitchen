package com.cloudkitchens.enums;

/**
 * #实时消费队列策略
 * 伪实时（定时扫描）
 * 实时（while(true){}）
 * socket长链接实时
 */
public enum RealTimeConsumeQueueStrategy {
    PSEUDO_REALTIME("PSEUDO_REALTIME"),//伪实时（定时扫描）
    REALTIME("REALTIME"),//实时（while(true){}）
    SOCKET_REALTIME("SOCKET_REALTIME");//socket长链接实时
    private String type;

    private RealTimeConsumeQueueStrategy(String type) {
        this.type = type;
    }
}
