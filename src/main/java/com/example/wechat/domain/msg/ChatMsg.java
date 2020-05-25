package com.example.wechat.domain.msg;


import lombok.Data;

import java.util.Date;

@Data
public class ChatMsg {
    //是否群聊
    private boolean groupchat;
    //群聊id
    private Integer groupchatid;
    //信息来自哪个用户
    private String fromusername;
    //发送信息给哪个用户
    private String tousername;
    //消息
    private String message;
    //消息时间
    private Date msgDate;

}
