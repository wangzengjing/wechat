package com.example.wechat.exception.handler;

import com.example.wechat.exception.AvatarException;
import com.example.wechat.security.output.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ApiResult handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return ApiResult.fail(e.getMessage());
    }

    /**
     * 头像图片生成异常
     */
    @ExceptionHandler(AvatarException.class)
    public ApiResult avatarException(AvatarException e){
        log.error(e.getMessage(), e);

        return ApiResult.fail(e.getMessage());
    }

}
