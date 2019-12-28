package com.shu.smallvideo.model;

import java.util.Date;
import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/26 9:51
 * @uint d9lab
 * @Description:
 */
public class Play {
    //总播放量
    double totalPlayNum;
    double avgPlayNum;
    Integer videoNum;
    Date startTime;
    Date endTime;
    String date;

    public double getTotalPlayNum() {
        return totalPlayNum;
    }

    public void setTotalPlayNum(double totalPlayNum) {
        this.totalPlayNum = totalPlayNum;
    }

    public double getAvgPlayNum() {
        return avgPlayNum;
    }

    public void setAvgPlayNum(double avgPlayNum) {
        this.avgPlayNum = avgPlayNum;
    }

    public Integer getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(Integer videoNum) {
        this.videoNum = videoNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
