package com.example.wechat.exception;

public class AvatarException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public AvatarException(String message)
    {
        this.message = message;
    }

    public AvatarException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public AvatarException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }
}
