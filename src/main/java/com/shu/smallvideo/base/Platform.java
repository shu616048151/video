package com.shu.smallvideo.base;

/**
 * @author shuxibing
 * @date 2019/12/26 15:51
 * @uint d9lab
 * @Description:
 */
public enum  Platform {
    Video("普通视频"),
    Youtube("油管"),
    XiGua("西瓜视频"),
    BiliBili("B站");

    private String name;
    Platform(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
