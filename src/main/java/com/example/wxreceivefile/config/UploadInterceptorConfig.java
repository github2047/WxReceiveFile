package com.example.wxreceivefile.config;


import com.example.wxreceivefile.Interceptor.WebInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private WebInterceptor uploadInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(uploadInterceptor)
                .addPathPatterns("/**")
//                .excludePathPatterns("/upload/uploadImage")
//                .excludePathPatterns("/upload/toImage")
//                .excludePathPatterns("/**")
                .excludePathPatterns("/upload/**")
                .excludePathPatterns("/static/**")//静态资源
                .excludePathPatterns("/page/*")//通用界面跳转
                .excludePathPatterns("/errorGq")//错误界面跳转
                .excludePathPatterns("/home")//认证界面
                .excludePathPatterns("/ding/ding")//钉钉认证
                .excludePathPatterns("/feiShu/code")//飞书认证
                .excludePathPatterns("/getQrCode")//获取企业微信配置信息
                .excludePathPatterns("/wx/Wx")//微信认证
                .excludePathPatterns("/getUserId");//企业微信认证

    }
}
