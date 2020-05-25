package com.example.wechat.domain;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "sys_user_contact")
public class SysUserContact {
    /**
     * 好友关联表ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户关联好友ID
     */
    @Column(name = "contact_id")
    private Integer contactId;

    /**
     * 用户好友昵称
     */
    @Column(name = "`nickname`")
    private String nickname;

}