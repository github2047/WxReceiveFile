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
       String pdfFilePath="D:\\uploadFile\\123\\a89c9fc1-b0cb-4b8c-95fc-97687631aca6Mybatis.pdf";
       System.out.println(pdfFilePath.substring(pdfFilePath.lastIndexOf("\\")+1,pdfFilePath.lastIndexOf(".")));
    }

}
