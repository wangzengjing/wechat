package com.example.wechat.domain.msg;


import lombok.Data;

import java.util.Date;

@Data
public class SessionExpired {

    //消息
    private String message;

    //消息时间
    private Date msgDate;
}
