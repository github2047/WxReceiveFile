package com.example.wxreceivefile.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wxreceivefile.mapper.PlatUserMapper;
import com.example.wxreceivefile.pojo.PlatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/feiShu")
public class feiShuController {
    @Value("${FS.AppId}")
    private String app_id;
    @Value("${FS.AppSecret}")
    private String app_secret;
    @Autowired
    private PlatUserMapper platUserMapper;

    @RequestMapping("/code")
    public Map fs(@RequestParam Object code) {
        log.info("code:"+code.toString());
        Map mapResult=new HashMap();
        //获取access_token
        String url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        //请求头
        HttpHeaders headers=new HttpHeaders();
        headers.set("Content-Type","application/json;charset=utf-8");
        //请求体
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("app_id",app_id);
        jsonObject.put("app_secret",app_secret);
        //主体
        HttpEntity<String> httpEntity=new HttpEntity<>(jsonObject.toString(),headers);
        String access_token="";
        ResponseEntity<Map> response=new ResponseEntity<Map>(HttpStatus.OK);
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            access_token = response.getBody().get("tenant_access_token").toString();
            log.info("access_token:"+access_token);
        }catch (Exception e)
        {
            log.warn("Token获取失败:"+response.getBody().toString());
            mapResult.put("msg","Token获取失败,请重新进入");
            mapResult.put("status",500);
            return mapResult;
        }
        //通过access_token和code获取userid
        String url1 = "https://open.feishu.cn/open-apis/authen/v1/access_token";
        RestTemplate restTemplate1 = new RestTemplate();
        restTemplate1.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        //请求头
        HttpHeaders headers1=new HttpHeaders();
        headers1.set("Authorization","Bearer "+access_token);
        headers1.set("Content-Type","application/json;charset=utf-8");
        //请求体
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("grant_type","authorization_code");
        jsonObject1.put("code", code);
        //主体
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject1.toString(), headers1);
        ResponseEntity<Map> responseuser=new ResponseEntity<Map>(HttpStatus.OK);
        try {
            responseuser = restTemplate1.exchange(url1, HttpMethod.POST, entity, Map.class);

        }
        catch (Exception e)
        {
            log.warn("userId获取失败:"+responseuser.toString());
            mapResult.put("msg","userId获取失败,请重新进入");
            mapResult.put("status",500);
            return mapResult;
        }
        Map userinfo = (Map) responseuser.getBody().get("data");
        String errCode=responseuser.getBody().get("code").toString();
        if(errCode.equals("0"))
        {
            log.info("飞书用户登录");
            log.info("name:"+userinfo.get("name").toString());
            log.info("openid:"+userinfo.get("open_id").toString());
            String openId=userinfo.get("open_id").toString();
            PlatUser user = platUserMapper.getInfo(openId, "Fs");
            if (user==null) {
                mapResult.put("status",500);
                mapResult.put("msg", "用户不存在");
                return mapResult;
            } else {
                mapResult.put("status",200);
                mapResult.put("userid",openId);
//                mapResult.put("name", userinfo.get("name").toString());
                return mapResult;
            }
        }else{
            log.warn("授权码错误:"+responseuser.getBody().toString());
            mapResult.put("status",500);
            mapResult.put("msg","授权码错误");
            return mapResult;
        }
    }
}
