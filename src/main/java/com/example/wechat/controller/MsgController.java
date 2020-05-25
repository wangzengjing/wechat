package com.example.wechat.controller;

import com.example.wechat.callback.WebSocketCallback;
import com.example.wechat.domain.msg.ChatMsg;
import com.example.wechat.domain.msg.MsgEventType;
import com.example.wechat.security.output.ApiResult;
import com.example.wechat.services.Impl.MsgService;
import com.example.wechat.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yeauty.pojo.Session;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    private MsgService msgService;





    /**
     * 获取离线消息
     * @return
     */
    @GetMapping("getOfflineMsg")
    public ApiResult getOfflineMsg(){

        String username = UserUtils.getUserName();
        //登录会话过期处理
        if(username.equals("anonymousUser")){
            return ApiResult.ok(1,"查询失败，登录会话已过期，请重新登录");
        }

        List<MsgEventType> offlineMsgByUserName = msgService.getOfflineMsgByUserName(username);

        return ApiResult.ok(0,"查询成功",offlineMsgByUserName);
    }


    @RequestMapping("/sendMsg")
    public ApiResult sendMsg(String message){
        Map<String, Session> sessions = WebSocketCallback.getSessions();

        if (sessions.containsKey("admin")){
            Session admin = sessions.get("admin");
            admin.sendText(message);
            return ApiResult.ok(0,"发送成功");
        }else{
            return ApiResult.fail(1,"admin已经下线");
        }


    }




}
