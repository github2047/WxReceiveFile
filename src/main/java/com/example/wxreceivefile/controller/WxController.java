package com.example.wxreceivefile.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wxreceivefile.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/wx")
public class WxController {
    //验证微信
    @GetMapping("/verifyWx")
    @ResponseBody
    public String verifyWXToken(HttpServletRequest request) throws AesException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce)) {
            System.out.println(echostr);
            return echostr;
        }
        return null;
    }

    //微信接收消息
    @RequestMapping("/verifyWx")
    @ResponseBody
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 设置一下相应的格式
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();// 获取resq的数据流
        Map<String, String> map;
        try {
            map = MessageUtil.xmlToMap(req);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            String format = map.get("Format");

//            String message = null;
//            if ("text".equals(msgType)) {
//                TextMessage text = new TextMessage();
//                text.setFromUserName(toUserName);
//                text.setToUserName(fromUserName);
//                text.setMsgType("text");
//                text.setCreateTime(new Date().getTime());
//                text.setContent("您发送的消息类型是:" + msgType + ";");
//                message = MessageUtil.textMessageToXml(text);// message来接收
//            }
//            if ("image".equals(msgType)) {
//                TextMessage text = new TextMessage();
//                text.setFromUserName(toUserName);
//                text.setToUserName(fromUserName);
//                text.setMsgType("text");
//                text.setCreateTime(new Date().getTime());
//                text.setContent("您发送的消息类型是:" + msgType + ";");
//                message = MessageUtil.textMessageToXml(text);// message来接收
//            }
//            if ("voice".equals(msgType)) {
//                TextMessage text = new TextMessage();
//                text.setFromUserName(toUserName);
//                text.setToUserName(fromUserName);
//                text.setMsgType("text");
//                text.setCreateTime(new Date().getTime());
//                text.setContent("您发送的消息类型是:" + msgType + ";");
//                message = MessageUtil.textMessageToXml(text);// message来接收
//            }
            System.out.println(map);
//            out.println(message);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
    @RequestMapping("/Wx")
    public String getUserId(String code, Model model){
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        //获取openid
        url = String.format(url, "wx8a0a151278949bbd","ebf79bbf2c69ee19e9a448b795b5c514", code);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        //获取用户openid
        String resp = null;
        try{
            resp = HttpClientUtils.getRestTemplate().getForObject(uri, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.error("Wx 调用接口异常：",e.getMessage());
        }
        Map map=new HashMap();
        if(resp!=null){
            JSONObject json = JSONObject.parseObject(resp);
            log.error("获取 openid");
            String openId = (String) json.get("openid");
            log.error("openid:"+openId);
            String access_token =(String) json.get("access_token");
            //获取accessToken
            String accessToken=getToken("wx8a0a151278949bbd","ebf79bbf2c69ee19e9a448b795b5c514");
            if(null!=access_token){
                //获取用户基本信息
                String userinfo=getUserInfo(accessToken,openId);
                if(null!=userinfo){
                    JSONObject json1 = JSONObject.parseObject(userinfo);
                    //获取用户nick
                    String nickname = (String) json.get("nickname");
                    log.info("微信，用户"+nickname+"登入,时间："+new Date());
                    map.put("userid",openId);
                    map.put("name",nickname);
                }else{
                    log.error("userinfo is null");
                }
            }else{
                log.error("token is null");
            }

        }else{
            log.error("openId","获取用户openid失败");
        }
        model.addAttribute("map",map);
        return "home";
    }
    public String getToken(String appid,String secret){
        String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        url = String.format(url, appid,secret);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        //获取用户信息 通过HttpClientUtils调用url返回扫码用户的userid
        String resp = null;
        try{
            resp = HttpClientUtils.getRestTemplate().getForObject(uri, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取token 接口异常：",e.getMessage());
        }
        if(resp!=null){
            JSONObject json = JSONObject.parseObject(resp);
            Integer errcode =(Integer) json.get("errcode");
            String errmsg =(String) json.get("errmsg");
            if(null==errcode){
                //获取accessToken
                String access_token =(String) json.get("access_token");
                return access_token;
            }else{
                log.error("获取token失败:"+errmsg);
            }
        }else{
            log.error("获取token 异常");
        }
        return null;
    }
    public String getUserInfo(String token ,String openid){
        String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
        url = String.format(url, token,openid);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        //获取用户信息 通过HttpClientUtils调用url返回扫码用户的userinfo
        String resp = null;
        try{
            resp = HttpClientUtils.getRestTemplate().getForObject(uri, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取userinfo 接口异常：",e.getMessage());
        }
        if(resp!=null){
            JSONObject json = JSONObject.parseObject(resp);
            Integer errcode =(Integer) json.get("errcode");
            String errmsg =(String) json.get("errmsg");
            if(null==errcode){
                //获取info
                return resp;
            }else{
                log.error("获取info失败:"+errmsg);
            }
        }else{
            log.error("获取info 异常");
        }
        return null;
    }
}
