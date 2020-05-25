package com.example.wechat.security.sessionstrategy;

import com.example.wechat.security.output.ApiResult;
import com.example.wechat.security.utils.ResponseUtils;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {

        HttpServletResponse response = event.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        ResponseUtils.out(response, ApiResult.fail(1,"会话已过期"));
    }
}
