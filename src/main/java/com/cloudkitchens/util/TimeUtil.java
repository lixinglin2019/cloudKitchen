package com.cloudkitchens.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {


    /**
     * 单位：毫秒
     * @return 产生3~15秒的耗时
     */
    public static long getRandomUseTime(int left, int right) {
        if (left > right) {
            try {
                throw new Exception(" left must little than right");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (left == right) {
            return left;
        } else {
            int gap = right - left;
            //产生一个3~15之间的随机数 (数据类型)(最小值+Math.random()*(最大值-最小值+1))
            double random = Math.random();//0~1之间的随机数
            Double v = random * (gap + 1);//对该值取整
            int i = v.intValue();//直接取整
            long result = left + i;
            Long randomTime = result * 1000;//毫秒
            return randomTime;
        }

    }

    public static void main(String[] args) throws Exception {
        long randomUseTime = getRandomUseTime(3, 15);
        System.out.println(randomUseTime);
    }


    /**
     * 3~15秒 之间的一个时间
     *
     * @return
     */
    public static long courierArriveKitchenRandomTime(Long userTime) {
        long arriveTimeMillis = System.currentTimeMillis() + userTime;//当前时间顺延
        return arriveTimeMillis;
    }

    /**
     * @param time 毫秒
     * @return
     */
    public static String getFormatData(Long time){
        Date date = new Date();
        if (time != null) {
            date.setTime(time);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        String format = sdf.format(date);
        return format;
    }
}
