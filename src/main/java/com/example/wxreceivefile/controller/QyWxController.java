package com.example.wxreceivefile.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wxreceivefile.mapper.PlatUserMapper;
import com.example.wxreceivefile.pojo.PlatUser;
import com.example.wxreceivefile.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;



import java.net.URI;
import java.util.*;

@Controller
@Slf4j
public class QyWxController {
    @Value("${QyWx.QyWxCorpId}")
    private String corpId;
    @Value("${QyWx.QyWxCorpSecret}")
    private String secret;
    @Value("${QyWx.QyWxAgentId}")
    private String agentId;
    @Value("${QyWx.QyWxRedirectUri}")
    private String redirectUri;
    @Autowired
    public RedisUtil redisUtil;
    @Autowired
    private PlatUserMapper platUserMapper;



//    //验证企业微信
//    @GetMapping("/verifyQyWx")
//    @ResponseBody
//    public String VerifyAPI(HttpServletRequest request) throws AesException,Exception {
//        String msgSignature = request.getParameter("msg_signature");
//        String timeStamp = request.getParameter("timestamp");
//        String nonce = request.getParameter("nonce");
//        String echoStr = request.getParameter("echostr");
//        WXBizMsgCrypt wxcpt = QyWxUtil.getWxCpt();
//        String sEchoStr; //需要返回的明文
//        try {
//            sEchoStr = wxcpt.VerifyURL(msgSignature, timeStamp, nonce, echoStr);
//            System.out.println("verifyurl echostr: " + sEchoStr);
//            return sEchoStr;
//            // 验证URL成功，将sEchoStr返回
//            // HttpUtils.SetResponse(sEchoStr);
//        } catch (Exception e) {
//            //验证URL失败，错误原因请查看异常
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    //企业微信接收消息
//    @PostMapping("/verifyQyWx")
//    @ResponseBody
//    public void QyWxPost(HttpServletRequest req,HttpServletResponse response) throws Exception {
//        // 设置一下相应的格式
//        String msgSignature = req.getParameter("msg_signature");
//        String timeStamp = req.getParameter("timestamp");
//        String nonce = req.getParameter("nonce");
//        // post请求的密文数据
//        WXBizMsgCrypt wxcpt = QyWxUtil.getWxCpt();
//        String sReqData= QyWxUtil.readInputStream(req.getInputStream());
//        try {
//            String sMsg = wxcpt.DecryptMsg(msgSignature, timeStamp, nonce, sReqData);
//            Map map=QyWxUtil.multilayerXmlToMap(sMsg);
//            HashMap hashMap= (HashMap) map.get("xml");
//
//            //基本信息
//            String CreateTime=(String) hashMap.get("CreateTime");
//            String ToUserName=(String) hashMap.get("ToUserName");
//            String FromUserName=(String) hashMap.get("FromUserName");
//            String MsgType=(String) hashMap.get("MsgType");
//            String AgentID=(String) hashMap.get("AgentID");
//            String MsgId=(String) hashMap.get("MsgId");
//
//            String access_token = (String) redisUtil.get("qywx_access_token");
//            if(access_token==null){
//                log.error("QyWx access_token过期，重新获取");
//                String json = WxUtils.getAccessToken("wwb88c17271539988c", "ijUZ2C-rVXRMXozZ05m_-GRT6O5gwqMosgiFFKsqekY");
//                JSONObject jsonObject = JSONObject.parseObject(json);
//                access_token = (String) jsonObject.get("access_token");
//                Integer expires_in=(Integer) jsonObject.get("expires_in");
//                redisUtil.set("qywx_access_token",access_token,expires_in);
//            }
//            //获取临时文件 get
//            String url="https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token="+access_token+"&media_id=";
//            //文本
//            if ("text".equals(hashMap.get("MsgType"))) {
//                log.info("文本消息");
//                String Content=(String) hashMap.get("Content");
//                System.out.println(Content);
//            }
//            //图片
//            if("image".equals(hashMap.get("MsgType"))){
//                log.info("图片消息");
//                String MediaId= (String) hashMap.get("MediaId");
//                String PicUrl=(String) hashMap.get("PicUrl");
//
//                url=url+MediaId;
//                InputStream inputStream= HttpClientUtils.httpGet(url, new HashMap<>());
//
//                String path="src/main/resources/static/upload/images/"+ UUID.randomUUID()+".png";
//                File file = new File(path);
//                if (!file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//                file.createNewFile();
//
//                OutputStream os = new FileOutputStream(file);
//                BufferedOutputStream bos = new BufferedOutputStream(os);
//
//                byte[] buf = new byte[1024];
//                int length;
//                length = inputStream.read(buf, 0, buf.length);
//
//                while (length != -1) {
//                    bos.write(buf, 0, length);
//                    length = inputStream.read(buf);
//                }
//                bos.close();
//                os.close();
//                inputStream.close();
//                log.info("图片保存成功");
//            }
//            //语音
//            if("voice".equals(hashMap.get("MsgType"))){
//                log.info("语音消息");
//                //语音格式，如amr，speex等
//                String MediaId= (String) hashMap.get("MediaId");
//                String Format=(String) hashMap.get("Format");
//            }
//            //视频
//            if("video".equals(hashMap.get("MsgType"))){
//                log.info("视频消息");
//                String MediaId= (String) hashMap.get("MediaId");
//                //视频消息缩略图的媒体id
//                String ThumbMediaId= (String) hashMap.get("ThumbMediaId");
//                url=url+MediaId;
//                InputStream inputStream= HttpClientUtils.httpGet(url, new HashMap<>());
//                String path="src/main/resources/static/upload/videos/"+ UUID.randomUUID()+".mp4";
//                File file = new File(path);
//                if (!file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//                file.createNewFile();
//
//                OutputStream os = new FileOutputStream(file);
//                BufferedOutputStream bos = new BufferedOutputStream(os);
//
//                byte[] buf = new byte[1024];
//                int length;
//                length = inputStream.read(buf, 0, buf.length);
//
//                while (length != -1) {
//                    bos.write(buf, 0, length);
//                    length = inputStream.read(buf);
//                }
//                bos.close();
//                os.close();
//                inputStream.close();
//                log.info("视频保存成功");
//            }
//            //位置
//            if("location".equals(hashMap.get("MsgType"))){
//                log.info("位置消息");
////                Location_X	地理位置纬度
////                Location_Y	地理位置经度
////                Scale	地图缩放大小
////                Label	地理位置信息
////                MsgId	消息id，64位整型
////                AppType	app类型，在企业微信固定返回wxwork，在微信不返回该字段
//            }
//            //链接
//            if("link".equals(hashMap.get("MsgType"))){
//                log.info("链接消息");
////                Title	标题
////                Description	描述
////                Url	链接跳转的url
////                PicUrl	封面缩略图的url
//            }
//            System.out.println(hashMap);
//        } catch (Exception e) {
//            log.error("解密失败:"+e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @RequestMapping("/getQrCode")
    @ResponseBody
    public Map getQrCode(){
        Map map = new HashMap();
        map.put("appid",corpId);
        map.put("agentid",agentId);
        map.put("redirect_uri",redirectUri);
        return map;
    }

    //通过code和access_token获取扫码用户的userid
    @RequestMapping("/getUserId")
    public String getUserId(String code, Model model){
        String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
        //获取access_token在redis中 如果没有就重新获取
        String access_token = (String) redisUtil.get("qywx_access_token");
        if(access_token==null){
            log.error("QyWx access_token过期，重新获取");
            String json = WxUtils.getAccessToken(corpId, secret);
            JSONObject jsonObject = JSONObject.parseObject(json);
            access_token = (String) jsonObject.get("access_token");
            Integer expires_in=(Integer) jsonObject.get("expires_in");
            redisUtil.set("qywx_access_token",access_token,expires_in);
        }
        url = String.format(url, access_token, code);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        //获取用户信息 通过HttpClientUtils调用url返回扫码用户的userid
        String resp = null;
        try{
            resp = HttpClientUtils.getRestTemplate().getForObject(uri, String.class);
        }catch (Exception e){
            e.printStackTrace();
            log.error("QyWx 调用接口异常：",e.getMessage());
        }
        Map map=new HashMap();
        if(resp!=null){
            JSONObject json = JSONObject.parseObject(resp);
            Integer errcode = (Integer) json.get("errcode");
            String userId = "";
            String errmsg = "";
            if(errcode!=0) {
                errmsg = (String) json.get("errmsg");
                log.error("QyWx 获取用户的userid错误："+errmsg);
                map.put("openId",errmsg);
            }else{
                userId = (String) json.get("UserId");
                String userInfo = getUserInfo(userId);
                JSONObject jsonObject = JSONObject.parseObject(userInfo);
                String userid= (String) jsonObject.get("userid");
                String name=(String) jsonObject.get("name");

                log.info("企业微信，用户"+name+"登入\n,时间："+new Date());
                PlatUser user = platUserMapper.getInfo(userid, "QyWx");
                if (user==null) {
                   log.error("该用户不在表中");
                   model.addAttribute("msg","你不在该应用中");
                   return "error";
                } else {
                    map.put("userid",userid);
                    map.put("name",name);
                }
            }
        }else{
            map.put("openId","获取用户信息失败");
        }
        model.addAttribute("map",map);

        return "home";
    }
    //通过userid获取用户的详细信息
    @RequestMapping("/getUserInfo")
    @ResponseBody
    public String getUserInfo(String userId){
        String url="https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
        //获取access_token在redis中 如果没有就重新获取
        String access_token =(String) redisUtil.get("qywx_access_token");
        if(access_token==null){
            log.error("QyWx access_token过期，重新获取");
            String json = WxUtils.getAccessToken(corpId, secret);
            JSONObject jsonObject = JSONObject.parseObject(json);
            access_token = (String) jsonObject.get("access_token");
            Integer expires_in=(Integer) jsonObject.get("expires_in");
            redisUtil.set("qywx_access_token",access_token,expires_in);
        }
        url = String.format(url, access_token, userId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        String resp=null;
        try{
            resp = HttpClientUtils.getRestTemplate().getForObject(uri, String.class);
        }catch (Exception e){
            log.error("QyWx 调用byUserid接口异常:",e.getMessage());
            e.printStackTrace();
        }
        if(resp!=null){
            JSONObject json = JSONObject.parseObject(resp);
            Integer errcode = (Integer) json.get("errcode");
            String errmsg = "";
            if(errcode!=0) {
                errmsg = (String) json.get("errmsg");
                log.error("QyWx 通过userid获取用户信息错误：",errmsg);
            }else{
                return json.toString();
            }
        }
        return resp;
    }

}
