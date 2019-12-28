package com.shu.smallvideo.util;

/**
 * @author shuxibing
 * @date 2019/12/26 10:44
 * @uint d9lab
 * @Description:
 */
public class DateUtil {
    /**
     *
     * @param time 2019-11-10
     * @return 2019-11
     */
    public static String getMonth(String time){
        String month = time.substring(0, time.lastIndexOf("-"));
        return month;
    }
}
