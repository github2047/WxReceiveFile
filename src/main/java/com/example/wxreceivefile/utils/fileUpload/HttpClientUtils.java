package com.example.wxreceivefile.utils.fileUpload;


import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Source;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpClientUtils {
    //http post请求 urlStr:请求地址 params:请求参数
    public static String httpPost(String urlStr, Map<String,String> params){
        URL connect;
        StringBuffer data = new StringBuffer();
        try {
            connect = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)connect.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);//post不能使用缓存
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            OutputStreamWriter paramout = new OutputStreamWriter(
                    connection.getOutputStream(),"UTF-8");
            String paramsStr = "";   //拼接Post 请求的参数
            for(String param : params.keySet()){
                paramsStr += "&" + param + "=" + params.get(param);
            }
            if(!paramsStr.isEmpty()){
                paramsStr = paramsStr.substring(1);
            }
            paramout.write(paramsStr);
            paramout.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            paramout.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }
    //http get请求 urlStr:请求地址 map:header请求头的参数，示例：map.put("Content-Type","application/json");
    public static InputStream httpGet(String urlStr, HashMap<String, Object> map) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            for(String key : map.keySet()){
                connection.setRequestProperty(key, map.get(key).toString());
            }
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();

//            String path="src/main/resources/static/upload/"+ UUID.randomUUID()+".png";
//            File file = new File(path);
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            file.createNewFile();
//
//            OutputStream os = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(os);
//
//            byte[] buf = new byte[1024];
//            int length;
//            length = in.read(buf, 0, buf.length);
//
//            while (length != -1) {
//                bos.write(buf, 0, length);
//                length = in.read(buf);
//            }
//            bos.close();
//            os.close();
//
//            InputStreamReader isr = new InputStreamReader(in, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            StringBuilder sb = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            br.close();
//            isr.close();
//            in.close();
//            Map map1=new HashMap();
//            map1.put("InputStream",in);
//            map1.put("str",sb.toString());
            return in;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static RestTemplate getRestTemplate() {// 手动添加
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(120000);
        List<HttpMessageConverter<?>> messageConverters = new LinkedList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<Source>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
