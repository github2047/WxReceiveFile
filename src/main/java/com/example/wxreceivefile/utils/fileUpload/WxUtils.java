package com.example.wxreceivefile.utils.fileUpload;

import com.alibaba.fastjson.JSONObject;
import com.example.wxreceivefile.utils.fileUpload.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
@Slf4j
public class WxUtils {
    @Autowired
    private static StringRedisTemplate stringRedisTemplate;

    //获取access_token 企业微信
    public static String getAccessToken(String corpId, String secret) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
        url = String.format(url, corpId, secret);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        String resp = HttpClientUtils.getRestTemplate().getForObject(uri, String.class);
        JSONObject json = JSONObject.parseObject(resp);
        Integer errcode = (Integer) json.get("errcode");
        if (errcode != 0) {
            String errmsg = (String) json.get("errmsg");
            log.error("QyWx 获取access_token失败，errcode:{},errmsg:{}", errcode, errmsg);
        } else {
            String accessToken = (String) json.get("access_token");
            Integer expires_in = (Integer) json.get("expires_in");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("access_token", accessToken);
            jsonObject.put("expires_in", expires_in);
            return jsonObject.toJSONString();
        }
        return null;
    }



}