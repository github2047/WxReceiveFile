//package com.example.wxreceivefile.config;
//
//
//import com.example.wxreceivefile.utils.fileUpload.UploadInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class UploadInterceptorConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new UploadInterceptor())
////                .addPathPatterns("/**")
//                .excludePathPatterns("/static/**")//静态资源
//                .excludePathPatterns("/page/*")//通用界面跳转
//                .excludePathPatterns("/home")//认证界面
//                .excludePathPatterns("/ding/ding");//钉钉认证
//    }
//}
