package com.example.wxreceivefile;

import com.example.wxreceivefile.mapper.PlatUserMapper;
import com.example.wxreceivefile.pojo.PlatUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WxReceiveFileApplicationTests {
    @Autowired
    private PlatUserMapper platUserMapper;
    @Test
    void contextLoads() {
    }

}
