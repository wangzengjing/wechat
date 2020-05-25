package com.example.wechat;

import com.example.wechat.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WechatApplicationTests {

    @Test
    void contextLoads() {
        RedisUtils.delete("user");
    }

}
