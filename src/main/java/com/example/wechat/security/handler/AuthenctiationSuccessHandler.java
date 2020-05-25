package com.example.wechat.security.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wechat.domain.SysUser;
import com.example.wechat.security.domain.User;
import com.example.wechat.security.emum.ResultCode;
import com.example.wechat.security.output.ApiResult;
import com.example.wechat.security.utils.ResponseUtils;
import com.example.wechat.services.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
public class AuthenctiationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SysUserService sysUserService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

//        RequestCache cache = new HttpSessionRequestCache();
//        SavedRequest savedRequest = cache.getRequest(request, response);
//        // 如果来源请求为空则跳转到用户博客首页
//        String url = "";
//        if((savedRequest==null)){
//            url = "/blog/"+ SecurityUtil.getLoginUser();
//        }else{
//            url = savedRequest.getRedirectUrl();
//        }
//
//        System.out.println(url);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("response_code",0);
        jsonObject.put("response_msg","登录成功");

        JSONObject user = new JSONObject();

        user.put("username",authentication.getName());
        user.put("sessionId",request.getSession().getId());
        jsonObject.put("data",user);


        ResponseUtils.out(response, ApiResult.ok(0,"登录成功",user));


    }
}
