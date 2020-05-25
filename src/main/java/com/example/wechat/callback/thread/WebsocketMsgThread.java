package com.example.wechat.callback.thread;

import com.example.wechat.services.Impl.MsgService;

import org.yeauty.pojo.Session;

import java.util.Map;

public class WebsocketMsgThread extends Thread{


    private MsgService msgService = null;
    private Map<String, Session> userSessionMap = null;
    private String message;

    public WebsocketMsgThread(MsgService msgService,Map<String, Session> userSessionMap,String message) {
        //指定线程名称
        super.setName("WebsocketMsgThread");
        this.msgService = msgService;
        this.userSessionMap = userSessionMap;
        this.message = message;

    }

    @Override
    public void run() {
        msgService.dealWithMsg(userSessionMap,message);

    }
}
