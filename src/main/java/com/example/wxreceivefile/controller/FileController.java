package com.example.wxreceivefile.controller;

import com.example.wxreceivefile.utils.PDFFileUtils;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${uploadDir}")
    String uploadDir;

    @RequestMapping("/upload")
    public Map uploadImages(@RequestParam("files") MultipartFile[] files, @RequestParam(value = "userid",required = false) String userid){
        Map resultMap = new HashMap();
        try {
            // 输入流
            String pdfPath = uploadDir + userid + "/" + UUID.randomUUID() + ".pdf";
            File pdfFile = new File(pdfPath);
            if (!pdfFile.getParentFile().exists()) {
                pdfFile.getParentFile().mkdirs();
                log.info("创建文件夹");
            }
            pdfFile.createNewFile();
            FileOutputStream fos1 = new FileOutputStream(pdfFile);
            // 创建文档
            Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
            //doc.open();
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos1);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;
            // 获取图片文件夹对象
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                try {
                    byte[] bytes = file.getBytes();
                    String path = uploadDir + userid + "/" + UUID.randomUUID() + fileName;
                    File newFile = new File(path);
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                        log.info("创建文件夹");
                    }
                    newFile.createNewFile();
                    log.info("创建文件");
                    FileOutputStream fos = new FileOutputStream(newFile);
                    log.info("写入文件");
                    fos.write(bytes);
                    fos.close();
                    log.info("保存文件成功：" + path);
                    img = ImageIO.read(newFile);
                    doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                    // 实例化图片
                    image = Image.getInstance(path);
                    doc.open();
                    doc.add(image);
                    resultMap.put("code",200);
                    resultMap.put("data",pdfPath.substring(13,pdfPath.length()));
                } catch (Exception e) {
                    e.printStackTrace();
                    resultMap.put("code", 500);
                    resultMap.put("msg", "上传失败" + e.getMessage());
                    log.info("保存文件失败：" + e.getMessage());
                    return resultMap;
                }
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    @RequestMapping("/getPdf")
    private Map getPdf(@RequestBody Map map){
        Map resultMap=new HashMap<>();
        String path= (String) map.get("path");
        String base64 = PDFFileUtils.PDFToBase64(path);
        if(base64!=null){
            resultMap.put("code",200);
            resultMap.put("data",base64);
        }else{
            resultMap.put("code",500);
            resultMap.put("msg","转码失败");
        }
        return resultMap;
    }
}
