package com.example.wechat.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * 此工具类需要经过Spring Security过滤链才可生效,就是在controller里面调用
 */
public class UserUtils {

    public static String getUserName(){
        String username = "";
        //判断是否在会话中，不在会话中用户名为anonymousUser
        SecurityContext context = SecurityContextHolder.getContext();
        if(null != context) {
            Authentication authentication = context.getAuthentication();
            username = authentication.getName();

        }

        return username;
    }
}
