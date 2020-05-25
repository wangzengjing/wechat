package com.example.wechat.services.Impl;

import com.example.wechat.callback.thread.UpdateAvatarThread;
import com.example.wechat.commom.ValidConstant;
import com.example.wechat.domain.SysUser;
import com.example.wechat.domain.vaild.ImageCode;
import com.example.wechat.mapper.SysUserMapper;
import com.example.wechat.security.output.ApiResult;
import com.example.wechat.services.SysUserService;
import com.example.wechat.utils.UpdateGroupLogoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class ValidService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${image.path}")
    private String imagePath;

    private  ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,
            10,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()) ;


    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


    /**
     * 验证码校验
     */
    @Transactional(readOnly = false)
    public ApiResult validata(HttpServletRequest req,String username,String password, String validcode){

        //从内存中获取相应的验证码对象
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(new ServletWebRequest(req), ValidConstant.SESSION_KEY);

        if (StringUtils.isBlank(validcode) ){
            return ApiResult.fail(1,"验证码不可以为空");
        }

        //验证码不匹配
        if ( null == codeInSession || !StringUtils.equals(codeInSession.getCode(), validcode)) {
            return ApiResult.fail(1,"验证码不匹配");
        }

        if(codeInSession.isExpired()){
            return ApiResult.fail(1,"验证码已过期，请刷新验证码");
        }


        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setViewname(username);
        sysUser.setPassword(bCryptPasswordEncoder.encode(password));
        sysUser.setAvatar(username+".jpg");
        sysUser.setUpdateTime(new Date());
        sysUserMapper.insert(sysUser);

        threadPool.execute(new UpdateAvatarThread(imagePath,username));

        sessionStrategy.removeAttribute(new ServletWebRequest(req),ValidConstant.SESSION_KEY);

        return ApiResult.ok(0,"注册成功!");
    }


}
