package com.example.wxreceivefile.utils.fileUpload;

import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class Pdf2Img {
    @Value("${uploadDir}")
    private String uploadDir;

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param pdfFilePath    pdf完整路径
     * @param dpi            dpi越大转换后越清晰，相对转换速度越慢
     */
    public static Object toImg(String pdfFilePath, int dpi,int len) {
        //定义集合，保存返回图片路径
        String dstImgFolder=pdfFilePath.substring(pdfFilePath.lastIndexOf("/")+1,pdfFilePath.lastIndexOf("."));//        String dstImgFolder="img";
        List<String> fileList = new ArrayList<String>();
        File file = new File(pdfFilePath);
        String parentFile= file.getParentFile()+"/"+dstImgFolder;
        File fileImg=new File(parentFile);
        if(!fileImg.exists()){
            fileImg.mkdirs();
        }
        PDDocument pdDocument;
        UUID uuid = UUID.randomUUID();
        String uuId = uuid.toString();
        try {
            String imgFolderPath = dstImgFolder + File.separator + uuId;// 获取图片存放的文件夹路径
            String imgPdfPath = file.getParent();
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            PdfReader reader = new PdfReader(pdfFilePath);
            //pdf页数
            int pages = reader.getNumberOfPages();
            map.put("total",pages);
            int start=num*5;
            int end=start+5;
            if(end>pages){
                end=pages;
            }
            reader.close();
            StringBuilder imgFilePath;
            for (int i = start; i < end; i++) {
                String imgFilePathPrefix = imgPdfPath + File.separator + imgFolderPath;
                imgFilePath = new StringBuilder();
                imgFilePath.append(imgFilePathPrefix);
                imgFilePath.append("_");
                imgFilePath.append((i + 1));
                imgFilePath.append(".png");
                File dstFile = new File(imgFilePath.toString());
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                ImageIO.write(image, "png", dstFile);
                log.info(String.valueOf(imgFilePath));
                fileList.add(imgFilePath.substring(len));
            }
            pdDocument.close();
            log.info("PDF文档转PNG图片成功！");
            map.put("fileList",fileList);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
