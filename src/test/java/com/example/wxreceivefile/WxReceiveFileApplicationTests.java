package com.example.wxreceivefile;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WxReceiveFileApplicationTests {

    @Test
    void contextLoads() {
        String path="src/main/resources/static/upload/file/2047/12e910d9-b1e2-4732-b6e7-d89600a1a082.pdf";
        System.out.println(path.substring(18,path.length()));
    }

}
