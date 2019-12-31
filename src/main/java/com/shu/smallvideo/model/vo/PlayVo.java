package com.shu.smallvideo.model.vo;

import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.model.Play;

import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/26 9:50
 * @uint d9lab
 * @Description:
 */
public class PlayVo {
    Integer id;
    String userName;
    Platform platform;
    List<Play> playList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Play> getPlayList() {
        return playList;
    }

    public void setPlayList(List<Play> playList) {
        this.playList = playList;
    }
}
