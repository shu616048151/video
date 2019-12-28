package com.shu.smallvideo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shu.smallvideo.base.Platform;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shuxibing
 * @date 2019/12/22 17:11
 * @uint d9lab
 * @Description: 用戶表
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    Integer id;
    @Column(name = "username")
    String userName;
    @Column(name = "create_time")
    Date createTime;
    @Column(name = "department")
    String department;

    @Column(name = "platform")
    @Enumerated(EnumType.ORDINAL)
    Platform platform;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }
}

