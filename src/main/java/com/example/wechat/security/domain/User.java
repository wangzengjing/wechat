package com.example.wechat.security.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
public class User {


    /**
     * 用户昵称
     */
    private String username;



    /**
     * 电子邮箱
     */

    private String email;

    /**
     * 手机号码
     */

    private String phone;

    /**
     * 用户性别
     */

    private String sex;

    /**
     * 头像路径
     */

    private String avatar;

    /**
     * 更新时间
     */

    private Date updateTime;

    /**
     * 备注
     */
    private String remakes;

    /**
     * 返回APP的sessionId
     */
    private String sessionId;
}
