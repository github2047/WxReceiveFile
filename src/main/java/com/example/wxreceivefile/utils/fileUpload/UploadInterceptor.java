//package com.example.wxreceivefile.utils.fileUpload;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.lang.reflect.Method;
//
//@Slf4j
//public class UploadInterceptor implements HandlerInterceptor {
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
////        HandlerMethod handlerMethod = (HandlerMethod) handler;
////        Method method = handlerMethod.getMethod();
////        String methodName = method.getName();
//        HttpSession session = request.getSession();
//        if (null == session.getAttribute("loginName")) {
//            response.sendRedirect("/page/error");
//            System.out.println("请求路径：" + request.getRequestURL());
//            return false;
//        }
////        } else {
////
//////            if (methodName.equals("test")) {
//////
//////            }
////        }
//        return true;
//    }
//}
//
//
