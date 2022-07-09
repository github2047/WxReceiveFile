package com.example.wxreceivefile.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wxreceivefile.mapper.PlatUserMapper;
import com.example.wxreceivefile.pojo.PlatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@CrossOrigin
@RestController
@RequestMapping("/ding")
public class DingController {
    @Value("${DD.AppKey}")
    private String appKey;
    @Value("${DD.AppSecret}")
    private String appSecret;
    @Autowired
    private PlatUserMapper platUserMapper;

    @RequestMapping("/ding")
    public Map ding(@RequestParam Object code) {
        log.info("code:" + code.toString());
        Map mapResult = new HashMap();
        //获取access_token
        String url = "https://oapi.dingtalk.com/gettoken?appkey=" + appKey + "&appsecret=" + appSecret;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<Map> response = new ResponseEntity<Map>(HttpStatus.OK);
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);

        } catch (Exception e) {
            log.warn("Token获取失败:"+response.getBody().toString());
            mapResult.put("msg", "Token获取失败,请重新进入");
            mapResult.put("status", 500);
            return mapResult;
        }
        String access_token = response.getBody().get("access_token").toString();

        //通过access_token和code获取userid
        String url1 = "https://oapi.dingtalk.com/topapi/v2/user/getuserinfo?access_token=" + access_token;
        RestTemplate restTemplate1 = new RestTemplate();
        restTemplate1.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), null);
        ResponseEntity<Map> responseuser = new ResponseEntity<Map>(HttpStatus.OK);
        try {
            responseuser = restTemplate1.exchange(url1, HttpMethod.POST, entity, Map.class);
        } catch (Exception e) {
            log.warn("userId获取失败:"+responseuser.toString());
            mapResult.put("msg", "userId获取失败,请重新进入");
            mapResult.put("status", 500);
            return mapResult;
        }

        Map userinfo = (Map) responseuser.getBody().get("result");
        String errCode = responseuser.getBody().get("errcode").toString();
        if (errCode.equals("0")) {
            log.info("钉钉用户登录");
            log.info("name:"+userinfo.get("name").toString());
            log.info("unionid:"+userinfo.get("unionid").toString());
            String unionid = userinfo.get("unionid").toString();
            //查找用户是否在数据库中
            PlatUser user = platUserMapper.getInfo(unionid, "Dd");
            if (user==null) {
                mapResult.put("status", 500);
                mapResult.put("msg", "用户不存在");
                return mapResult;
            } else {
                mapResult.put("status", 200);
                mapResult.put("userid", unionid);
//                mapResult.put("name", userinfo.get("name").toString());
                return mapResult;
            }
        } else {
            log.warn("授权码错误:"+responseuser.getBody().toString());
            mapResult.put("status", 500);
            mapResult.put("msg", "授权码错误");
            return mapResult;
        }
    }
}
