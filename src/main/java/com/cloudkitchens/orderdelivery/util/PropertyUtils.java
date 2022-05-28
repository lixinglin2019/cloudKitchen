package com.cloudkitchens.orderdelivery.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件 工具类
 */
public class PropertyUtils {

    // 订单配置文件
    private static String dispatchCourierStrategy = "config/dispatchCourierStrategy.properties";
    // 邮件配置文件
    private static String ratelimitStrategy = "config/ratelimitStrategy.properties";

    private static Properties dispatchCourierProp = null;
    private static Properties ratelimitProp = null;

    /**
     * 读取订单费用相关的配置信息
     *
     * @author YuHaiQing
     */
    public static String getDispathCourierProp(String key) {

        if (dispatchCourierProp == null) {
            dispatchCourierProp = getProp(dispatchCourierStrategy);
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
            ratelimitProp = getProp(ratelimitStrategy);
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
