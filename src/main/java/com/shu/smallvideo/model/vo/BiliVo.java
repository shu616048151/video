package com.shu.smallvideo.model.vo;

import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.model.Bili;
import com.shu.smallvideo.model.base.InputConverter;
import com.shu.smallvideo.model.base.OutputConverter;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author shuxibing
 * @date 2019/12/30 19:57
 * @uint d9lab
 * @Description:
 */
@ToString
@Data
public class BiliVo implements OutputConverter<BiliVo,Bili> {
    Integer id;
    Integer comment;
    Integer typeid;
    String title;
    String play;
    String pic;
    String description;
    String copyright;
    String review;
    String author;
    Integer mid;
    Long created;
    String length;
    Integer video_review;
    Integer aid;

    Date createTime;
    Date updateTime;
    String createdStr;
    Integer user;
    Platform platform;
    String url;


}
