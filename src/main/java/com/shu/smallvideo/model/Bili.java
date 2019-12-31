package com.shu.smallvideo.model;

import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.model.base.InputConverter;
import com.shu.smallvideo.model.base.OutputConverter;
import com.shu.smallvideo.model.vo.BiliVo;
import lombok.Data;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shuxibing
 * @date 2019/12/30 16:03
 * @uint d9lab
 * @Description:
 */
@ToString
public class Bili implements InputConverter<BiliVo> {
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
    String createdStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }


    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Integer getVideo_review() {
        return video_review;
    }

    public void setVideo_review(Integer video_review) {
        this.video_review = video_review;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Date getCreateTime() {
        createTime=new Date((created*1000));
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatedStr() {
        createTime=new Date((created*1000));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        createdStr = simpleDateFormat.format(createTime);
        return createdStr;
    }

}
