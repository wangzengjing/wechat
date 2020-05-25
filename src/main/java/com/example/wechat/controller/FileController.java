package com.example.wechat.controller;

import com.example.wechat.security.output.ApiResult;
import com.example.wechat.services.Impl.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@RestController
@RequestMapping("/user")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 保存头像
     * @param request
     * @param username
     * @return
     * @throws Exception
     */
    @PostMapping("/updateAvatar")
    public ApiResult savePicByIo(HttpServletRequest request, String username) throws Exception{
        log.info("用户{}开始更换头像",username);
        ApiResult result = fileService.savePicByIo(request, username);
        return result;
    }
}
