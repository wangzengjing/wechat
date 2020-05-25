package com.example.wechat.domain;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "sys_user_groud")
public class SysUserGroud {
    /**
     * 表主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户账号
     */
    @Column(name = "username")
    private String username;

    /**
     * 群聊id
     */
    @Column(name = "groud_id")
    private String groudId;

    /**
     * 群聊用户昵称
     */
    @Column(name = "nickname")
    private String nickname;
}