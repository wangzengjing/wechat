package com.example.wechat.callback;



import com.alibaba.fastjson.JSON;
import com.example.wechat.callback.thread.WebsocketMsgThread;
import com.example.wechat.commom.EventTypeConstant;
import com.example.wechat.domain.msg.MsgEventType;
import com.example.wechat.domain.msg.SessionExpired;
import com.example.wechat.services.Impl.MsgService;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import javax.annotation.PreDestroy;
import java.io.IOException;

import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


@Slf4j
@ServerEndpoint(host = "${ws.host}",port = "${ws.port}",path = "${ws.path}")
public class WebSocketCallback {
    @Autowired
    private MsgService msgService;

    private static Map<String,Session>  userSessionMap = new ConcurrentHashMap();;

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,
            30,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()) ;


    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @PathVariable String arg, @PathVariable Map pathMap){
        session.setSubprotocols("stomp");

    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers,@RequestParam String username, @PathVariable String arg, @PathVariable Map pathMap){
        log.info("new connection-"+username);

        //添加websocket登入记录
        if (userSessionMap.containsKey(username)){
            Session oldSession = userSessionMap.get(username);
            logOutSession(oldSession);
        }
        userSessionMap.put(username,session);

        log.info("在线人数:"+userSessionMap.size());

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        log.info("one connection closed");
        //断开连接的时候删除session
        Collection<Session> col = userSessionMap.values();
        while(true == col.contains(session)) {

            col.remove(session);
        }

        log.info("在线人数:"+userSessionMap.size());

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {

       // threadPool.execute(new WebsocketMsgThread(msgService,userSessionMap,message));
        msgService.dealWithMsg(userSessionMap,message);

    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println(b);
        }
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

    public static Map<String,Session> getSessions(){
        return userSessionMap;
    }


      //根据value值获取到对应的一个key值


    public static String getKey(HashMap<String, Session> map, String value) {
        String key = null;
        //Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
        for (String getKey : map.keySet()) {
            if (map.get(getKey).equals(value)) {
                key = getKey;

            }

        }
        return key;
        //这个key肯定是最后一个满足该条件的key.

    }

    /**
     * 踢出用户回传信息
     * @param session
     */
    public void logOutSession(Session session){

        MsgEventType msgEventType = new MsgEventType();
        msgEventType.setEventType(EventTypeConstant.SESSIONEXPIRED);

        SessionExpired sessionExpired = new SessionExpired();
        sessionExpired.setMessage("您的账号在另一台手机中登录，如果不是本人操作，请及时修改密码！");
        sessionExpired.setMsgDate(new Date());

        msgEventType.setData(sessionExpired);

        session.sendText(JSON.toJSONString(msgEventType));
        session.close();
    }

    @PreDestroy
    public void destroyExecutor(){

        try {
            log.info("程序退出，关闭线程池...");
            threadPool.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            log.info("线程池关闭成功！");
        }
    }

    public static void main(String[] args) {
        MsgEventType msgEventType = new MsgEventType();
        msgEventType.setEventType(EventTypeConstant.SESSIONEXPIRED);

        SessionExpired sessionExpired = new SessionExpired();
        sessionExpired.setMessage("您的账号在另一台手机中登录，如果不是本人操作，请及时修改密码！");
        sessionExpired.setMsgDate(new Date());

        msgEventType.setData(sessionExpired);
        System.out.println(JSON.toJSONString(msgEventType));
    }

}


