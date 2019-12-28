package com.shu.smallvideo.vo;

import com.shu.smallvideo.base.Platform;

/**
 * @author shuxibing
 * @date 2019/12/22 16:09
 * @uint d9lab
 * @Description:
 */
public class VideoVo {
    Integer id;
    Integer user;
    String username;
    Platform platform;
    String title;
    String url;
    String play;
    String time;
    String timeLength;
    String photoThumb;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getPhotoThumb() {
        return photoThumb;
    }

    public void setPhotoThumb(String photoThumb) {
        this.photoThumb = photoThumb;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}
