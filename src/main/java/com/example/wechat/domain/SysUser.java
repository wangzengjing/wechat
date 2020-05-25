package com.example.wechat.domain;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
@Table(name = "sys_user")
public class SysUser {

    public interface UserCommon{};


    /**
     * 用户ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 登录账号
     */
    //@JsonView(UserCommon.class)
    @Column(name = "username")
    private String username;

    /**
     * 用户昵称
     */
    @Column(name = "viewname")
    private String viewname;

    /**
     * 登录密码
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 电子邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 用户性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 头像路径
     */
    //@JsonView(UserCommon.class)
    @Column(name = "avatar")
    private String avatar;

    /**
     * 用户状态
     */
    @Column(name = "`status`")
    private String status;

    /**
     * 最后登录IP
     */
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_date")
    private Date lastLoginDate;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @Column(name = "remakes")
    private String remakes;
}