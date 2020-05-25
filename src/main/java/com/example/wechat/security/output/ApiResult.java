package com.example.wechat.security.output;


import com.example.wechat.domain.SysUser;
import com.example.wechat.security.emum.ResultCode;
import com.fasterxml.jackson.annotation.JsonView;

/**
 *  <p> API返回参数 </p>
 *
 */

public class ApiResult {
    /**
     * 消息内容
     */
    //@JsonView(SysUser.UserCommon.class)
    private String response_msg;

    /**
     * 成功或有效为1，失败或无效为0，token过期
     */
   // @JsonView({SysUser.UserCommon.class})
    private Integer response_code;

    /**
     * 响应中的数据
     */
  //  @JsonView(SysUser.UserCommon.class)
    private Object data;

    public static ApiResult expired(String message) {
        return new ApiResult(-1, message, null);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(ResultCode.FAILURE.getCode(), message, null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code
     * @param message:
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult fail(Integer code, String message) {
        return new ApiResult(code, message, null);
    }

    public static ApiResult ok(String message) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message, null);
    }

    public static ApiResult ok() {
        return new ApiResult(ResultCode.SUCCESS.getCode(), "OK", null);
    }

    public static ApiResult build(Integer code, String msg, Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static ApiResult ok(String message, Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 自定义返回码
     */
    public static ApiResult ok(Integer code, String message) {
        return new ApiResult(code, message);
    }

    /**
     * 自定义
     *
     * @param code：验证码
     * @param message：返回消息内容
     * @param data：返回数据
     * @return: com.zhengqing.modules.common.dto.output.ApiResult
     */
    public static ApiResult ok(Integer code, String message, Object data) {
        return new ApiResult(code, message, data);
    }

    public ApiResult() { }

    public static ApiResult build(Integer code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public ApiResult(Integer code, String msg, Object data) {
        this.response_code = code;
        this.response_msg = msg;
        this.data = data;
    }

    public ApiResult(Object data) {
        this.response_code = ResultCode.SUCCESS.getCode();
        this.response_msg = "OK";
        this.data = data;
    }

    public ApiResult(String message) {
        this(ResultCode.SUCCESS.getCode(), message, null);
    }

    public ApiResult(String message, Integer code) {
        this.response_msg = message;
        this.response_code = code;
    }

    public ApiResult(Integer code, String message) {
        this.response_code = code;
        this.response_msg = message;
    }

    public String getResponse_msg() {
        return response_msg;
    }

    public void setResponse_msg(String response_msg) {
        this.response_msg = response_msg;
    }

    public Integer getResponse_code() {
        return response_code;
    }

    public void setResponse_code(Integer response_code) {
        this.response_code = response_code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}