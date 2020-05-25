package com.example.wechat.services.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wechat.commom.EventTypeConstant;
import com.example.wechat.domain.msg.ChatMsg;
import com.example.wechat.domain.SysUserGroud;
import com.example.wechat.domain.msg.MsgEventType;
import com.example.wechat.mapper.SysUserGroudMapper;
import com.example.wechat.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.yeauty.pojo.Session;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息处理
 */
@Service
public class MsgService {

    @Resource
    private SysUserGroudMapper sysUserGroudMapper;


    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,
            30,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()) ;



    /**
     * 根据用户名查询离线消息
     * @param userName
     * @return
     */
    public List<MsgEventType> getOfflineMsgByUserName(String userName){
        //Redis查询
        if(!RedisUtils.hasKey(userName)){
            return null;
        }
        List<MsgEventType> offlineMsg = (List<MsgEventType>) RedisUtils.get(userName);

        //删除Redis记录
        RedisUtils.delete(userName);

        return offlineMsg;
    }


    /**
     * 消息处理
     * @param userSessionMap
     * @param message
     */
    public void dealWithMsg(Map<String, Session> userSessionMap,String message){

        MsgEventType msgEventType = JSONObject.parseObject(message, MsgEventType.class);

        switch (msgEventType.getEventType()){
            //聊天消息处理
            case EventTypeConstant.CHATMSG:

                threadPool.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dealWithChatMsg(userSessionMap,msgEventType.getData(),msgEventType);
                    }
                }));

                break;

            case EventTypeConstant.ADDCONTACT:

                break;
        }


    }

    /**
     * 聊天消息推送处理
     * @param userSessionMap
     * @param message
     */
    public void dealWithChatMsg(Map<String, Session> userSessionMap,Object message,MsgEventType msgEventType){

        String msg = JSON.toJSONString(message);
        ChatMsg chatMsg = JSON.parseObject(msg, ChatMsg.class);
        //设置消息回传时间
        chatMsg.setMsgDate(new Date());
        msgEventType.setData(chatMsg);
        //群聊消息处理
        if (chatMsg.isGroupchat()){
            dealWithGroudMsg(userSessionMap,chatMsg,msgEventType);
        }
        //一对一消息处理
        else{
            dealWithSingleMsg(userSessionMap,msgEventType,chatMsg.getTousername());
        }

    }

    /**
     * 群聊消息处理
     * @param userSessionMap
     * @param chatMsg
     */
    public void dealWithGroudMsg(Map<String, Session> userSessionMap,ChatMsg chatMsg,MsgEventType msgEventType){

        //查询归属这个群聊中的用户
        Example example = new Example(SysUserGroud.class);
        example.createCriteria().andEqualTo("groudId",chatMsg.getGroupchatid());
        List<SysUserGroud> userGrouds = sysUserGroudMapper.selectByExample(example);


        //根据id查询用户详细信息
        userGrouds.forEach(user->{

            //群聊不会将消息推送给发起人
            if (!user.getUsername().equals(chatMsg.getFromusername())){
                sendMsg(userSessionMap,msgEventType,user.getUsername());
            }

        });

    }


    /**
     * 一对一消息处理
     * @param userSessionMap
     * @param msgEventType
     */
    public void dealWithSingleMsg(Map<String, Session> userSessionMap,MsgEventType msgEventType,String tousername){
            //发送消息
            sendMsg(userSessionMap,msgEventType,tousername);
    }


    /**
     * 发送消息
     * @param userSessionMap
     * @param msgEventType
     * @param username
     */
    public void sendMsg(Map<String, Session> userSessionMap,MsgEventType msgEventType,String username){

        //查看是否在线
        if(userSessionMap.containsKey(username)){
            //向好友发送消息
            Session user = userSessionMap.get(username);

            user.sendText(JSON.toJSONString(msgEventType));
        }
        //不在线的情况下应该储存到redis弄成离线消息
        else{

            List<MsgEventType> offlineMsgList = new ArrayList<>();
            if(RedisUtils.hasKey(username)){
                offlineMsgList = (List<MsgEventType>) RedisUtils.get(username);
            }

            offlineMsgList.add(msgEventType);
            RedisUtils.set(username,offlineMsgList);
        }

    }


    public static void main(String[] args) {
        MsgEventType msgEventType = new MsgEventType();


        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setGroupchat(false);
        chatMsg.setGroupchatid(0);
        chatMsg.setFromusername("admin");
        chatMsg.setTousername("user");
        chatMsg.setMessage("666");
        chatMsg.setMsgDate(new Date());
        msgEventType.setData(chatMsg);

        msgEventType.setEventType(EventTypeConstant.CHATMSG);

        System.out.println(JSON.toJSONString(msgEventType));
    }

}
