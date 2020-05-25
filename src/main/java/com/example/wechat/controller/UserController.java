package com.example.wechat.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wechat.domain.ContactUser;
import com.example.wechat.domain.SysUser;
import com.example.wechat.security.output.ApiResult;
import com.example.wechat.services.GroudChatService;
import com.example.wechat.services.SysUserContactService;
import com.example.wechat.services.SysUserService;
import com.example.wechat.utils.RedisUtils;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserContactService sysUserContactService;

    @Autowired
    private GroudChatService groudChatService;

    @Autowired
    private RedisUtils redisUtils;




    /**
     * 好友关联
     * @param username
     * @param contactname
     * @return
     */
    @PostMapping("/addContact")
    public ApiResult addContact(String username,String contactname){
        //判空处理
        if(StringUtils.isBlank(username) || StringUtils.isBlank(contactname)){
            return ApiResult.fail(1,"用户账号或者好友账号为空!");
        }

        //添加关联
        sysUserContactService.addContact(username,contactname);

        return ApiResult.ok(0,"成功添加好友:"+contactname);
    }

    /**
     * 获取所有好友列表
     * @param username
     * @return
     */
    //@JsonView(SysUser.UserCommon.class)
    @GetMapping("/getAllContact")
    public ApiResult getAllContact(String username){
        //查询关联用户
        List<ContactUser> contactUsers = sysUserContactService.getContactUsersByUsername(username);

        return ApiResult.ok(0,"查询成功",contactUsers);
    }


    @PostMapping("/addGroudChat")
    public ApiResult addGroudChat(@RequestBody List<String> users){

        String groudId = groudChatService.addGroudChat(users);

        JSONObject rep = new JSONObject();
        rep.put("groudId",groudId);


        return ApiResult.ok(0,"创建成功",rep);
    }


    @RequestMapping("test")
    public void test(){
        redisUtils.get("user");
    }


}
