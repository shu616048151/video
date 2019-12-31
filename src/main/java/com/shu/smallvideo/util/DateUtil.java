package com.shu.smallvideo.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by QiuYW on 2016/5/26.
 */
public class DateUtil {

    /**
     * 获取指定日期的偏移日期
     * @param date Date类型
     * @param dayCount 正：前多少天，负：后多少天
     * @return Date类型
     */
    public static Date getDayBefore(Date date,int dayCount){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        c.set(Calendar.DATE, c.get(Calendar.DATE) - dayCount);
        return c.getTime();
    }

    /**
     * 获取指定日期的偏移小时
     * @param date Date类型
     * @param hourCount 正：后多少小时，负：前多少小时
     * @return Date类型
     */
    public static Date getHourAfter(Date date,int hourCount){
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) +hourCount);
        return c.getTime();
    }

    public static Date getNextHour(Date date){
        return getHourAfter(date,1);
    }

    /**
     * 获取上一天
     * @param date
     * @return
     */
    public static Date getLastDay(Date date){
        return getDayBefore(date, 1);
    }

    /**
     * 获取下一天
     * @param date
     * @return
     */
    public static Date getNextDay(Date date){
        return getDayBefore(date, -1);
    }
    /**
     * 获取前7天
     * @param date
     * @return
     */
    public static Date getDayBeforeSeven(Date date){
        return getDayBefore(date,6);
    }

    /**
     * 获取前30天
     * @param date
     * @return
     */
    public static Date getDayBeforeThirty(Date date){
        return getDayBefore(date,29);
    }

    /**
     * 获取前60天
     * @param date
     * @return
     */
    public static Date getDayBeforeSixty(Date date){
        return getDayBefore(date, 59);
    }

    /**
     * 判断是不是0时
     * @param date
     * @return
     */
    public static boolean isZero(Date date){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTime(date);
        if (calendar.get(Calendar.HOUR_OF_DAY)==0)return true;
        return false;
    }

    /**
     * 判断是不是同一个小时
     * @param date
     * @param compareDate
     * @return
     */
    public static boolean isSameHour(Date date,Date compareDate){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(compareDate);
        if (calendar.get(Calendar.DAY_OF_MONTH)==day&&calendar.get(Calendar.HOUR_OF_DAY)==hour)return true;
        return false;
    }

    /**
     * 判断是不是同一个天
     * @param date
     * @param compareDate
     * @return
     */
    public static boolean isSameDay(Date date,Date compareDate){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(compareDate);
        if (calendar.get(Calendar.DAY_OF_MONTH)==day) return true;
        return false;
    }

    /**
     * 一天零点
     *
     * @param
     * @return
     */
    public static Date getToDay(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getToMonth(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取一天的结束时间
     *
     * @param
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


}
