package com.example.wechat.domain;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "groud_chat")
public class GroudChat {
    /**
     * 表主键
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 群主id
     */
    @Column(name = "owner_id")
    private Integer ownerId;

    /**
     * 群聊名称
     */
    @Column(name = "groudname")
    private String groudname;

    /**
     * 头像路径
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 群公告
     */
    @Column(name = "announcement")
    private String announcement;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}