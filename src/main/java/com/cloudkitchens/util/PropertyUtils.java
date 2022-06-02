package com.cloudkitchens.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件 工具类
 */
public class PropertyUtils {

    // 订单配置文件
    private static String dispatchCourierStrategyProperties = "config/dispatchCourierStrategy.properties";
    // 邮件配置文件
    private static String ratelimitStrategyProperties = "config/ratelimitStrategy.properties";

    private static String orderTestProperties = "config/orderTest.properties";
    private static String realTimeConsumeQueueStrategyProperties = "config/realTimeConsumeQueueStrategy.properties";

    private static Properties dispatchCourierProp = null;
    private static Properties ratelimitProp = null;
    private static Properties orderTestProp = null;
    private static Properties realTimeConsumeQueueStrategyProp = null;

    public static String getRealTimeConsumeQueueStrategyProp(String key) {
        if (realTimeConsumeQueueStrategyProp == null) {
            realTimeConsumeQueueStrategyProp = getProp(realTimeConsumeQueueStrategyProperties);
        }
        return realTimeConsumeQueueStrategyProp.get(key).toString();
    }

    public static String getOrderTestProp(String key) {
        if (orderTestProp == null) {
            orderTestProp = getProp(orderTestProperties);
        }
        return orderTestProp.get(key).toString();
    }

    /**
     * 读取订单费用相关的配置信息
     *
     * @author YuHaiQing
     */
    public static String getDispathCourierProp(String key) {

        if (dispatchCourierProp == null) {
            dispatchCourierProp = getProp(dispatchCourierStrategyProperties);
        }
        return dispatchCourierProp.get(key).toString();
    }

    /**
     * 读取订单费用相关的配置信息
     *
     * @author YuHaiQing
     */
    public static String getRatelimitProp(String key) {

        if (ratelimitProp == null) {
            ratelimitProp = getProp(ratelimitStrategyProperties);
        }
        return ratelimitProp.get(key).toString();
    }

    /**
     * 根据URL 加载配置文件
     *
     * @author YuHaiQing
     */
    private static Properties getProp(String path) {

        try {
            return PropertiesLoaderUtils.loadAllProperties(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
