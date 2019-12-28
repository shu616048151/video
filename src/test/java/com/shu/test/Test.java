package com.shu.test;

import com.sun.xml.internal.ws.server.ServerRtException;
import org.springframework.util.SystemPropertyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shuxibing
 * @date 2019/12/25 20:16
 * @uint d9lab
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void test1(){
        String str="6406播放 · 2年前";
        int index = str.indexOf("万");
        if (index>0){
            Pattern pattern=Pattern.compile("\\d+.\\d+万");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()){
                String group = matcher.group();
                System.out.println(group.substring(0,group.lastIndexOf("万")));
            }
        }else {
            Pattern pattern=Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()){
                String group = matcher.group();
                System.out.println(group);
            }
        }
    }
    @org.junit.Test
    public void test2() throws ParseException {
        String str="12-31";
//        Calendar calendar=Calendar.getInstance();
//        int weekYear = calendar.getWeekYear();
//        int weeksInWeekYear = calendar.getWeeksInWeekYear();
//        System.out.println(weekYear);
//        System.out.println(weeksInWeekYear);
        boolean b = str.matches("^[0-9]{4}.*");
        if (!b){
            Calendar calendar=Calendar.getInstance();
            int weekYear = calendar.getWeekYear();
            str=weekYear+"-"+str;
        }
        String month=str.substring(0,str.lastIndexOf("-"));
        System.out.println(month);
    }


}
