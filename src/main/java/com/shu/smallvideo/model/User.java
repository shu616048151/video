package com.shu.smallvideo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shu.smallvideo.base.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "mid")
    Integer mid;

    public User(String userName, Date createTime, String department, Platform platform, Integer mid) {
        this.userName = userName;
        this.createTime = createTime;
        this.department = department;
        this.platform = platform;
        this.mid = mid;
    }
}

