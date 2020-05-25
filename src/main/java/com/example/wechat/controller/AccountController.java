package com.example.wechat.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.example.wechat.commom.ValidConstant;
import com.example.wechat.domain.SysUser;
import com.example.wechat.domain.vaild.ImageCode;
import com.example.wechat.security.output.ApiResult;
import com.example.wechat.services.Impl.ValidService;
import com.example.wechat.services.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 账号服务
 */
@RestController
@Slf4j
public class AccountController {


    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 验证码有效时间
     */
    @Value("${valid.time}")
    private int validTime;

    @Value("${valid.width}")
    private int validWith;

    @Value("${valid.height}")
    private int validHeight;

    @Value("${valid.codecount}")
    private int validCodeCount;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidService validService;


    @PostMapping("/register")
    public ApiResult register(HttpServletRequest req,String username,String password,String validcode){
        ApiResult result = null;
        SysUser user = sysUserService.getUserByUsername(username);

        if(null != user){
            result = ApiResult.fail(1,"此账号已经有人注册");
        }

        result = validService.validata(req,username,password,validcode);

        return result;
    }




    /**
     * 验证码生成
     * @param response
     * @throws Exception
     */
    @GetMapping("/validCode")
    public void validateCode(HttpServletRequest req,HttpServletResponse response)throws Exception {

        //生成验证码
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(validWith, validHeight, validCodeCount, 4);

        ImageCode imageCode = new ImageCode(shearCaptcha.getCode(), validTime*60);

        //缓存储存
        sessionStrategy.setAttribute(new ServletWebRequest(req), ValidConstant.SESSION_KEY, imageCode);

        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //输出浏览器
        OutputStream out=response.getOutputStream();
        shearCaptcha.write(out);
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }

        log.info("生成验证码："+shearCaptcha.getCode());
    }


}
