package com.example.wxreceivefile.Interceptor;

import com.example.wxreceivefile.utils.fileUpload.RedisUtil;
import com.example.wxreceivefile.utils.test.Utils;
import com.example.wxreceivefile.utils.fileUpload.IpAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class WebInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    private boolean checkIp(String ipAddress, String ipWhite) {
        if (!StringUtils.hasLength(ipWhite)) {
            //无IP白名单通过
            return true;
        }
        if (!StringUtils.hasLength(ipAddress) ) {
            //无IP地址不通过
            return false;
        }
        if ("127.0.0.1".equals(ipAddress) || "localhost".equals(ipAddress)) {
            //本机通过
            return true;
        }
        String[] split = ipWhite.split(",");
        List<String> ipWhiteList = Arrays.asList(split);
        return ipWhiteList.contains(ipAddress);
    }
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        String methodName = method.getName();
//        如果路径包含/upload/，说明是服务器下载文件接口，需要IP白名单校验
        if (request.getRequestURL().indexOf("/upload/") > -1) {
            String ipAddress = IpAddressUtil.getIpAddress(request);
            String ipWhiteList = Utils.getProperty("ipWhiteList");
            boolean isOnWhile = checkIp(ipAddress, ipWhiteList);
            if (!isOnWhile) {
                log.error("not allow" + ipAddress);
                throw new RuntimeException("not allow" + ipAddress);
            } else {
                return true;
            }
        }
//校验用户是否过期
        HttpSession session = request.getSession();
        String sessionId = request.getSession().getId();
        String otherUserId = String.valueOf(session.getAttribute("loginName"));
        if (null == otherUserId) {
            log.info("用户loginName为空");
            return false;
        }else{
            Object redisObj = redisUtil.get("session:"+sessionId);
            if (ObjectUtils.isEmpty(redisObj)) {
                log.info("redis用户信息已过期，{}", otherUserId);
                log.info(request.getContextPath());
                throw new RuntimeException("用户已过期，请重新登录！");
            }
            if (ObjectUtils.nullSafeEquals(redisObj, otherUserId)) {
                log.info("用户校验通过，{}", otherUserId);
                redisUtil.set("session:" + sessionId, otherUserId,60);
                return true;
            }
            return false;
        }
    }
}


