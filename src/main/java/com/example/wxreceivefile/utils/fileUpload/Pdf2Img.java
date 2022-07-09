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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        String dstImgFolder=pdfFilePath.substring(pdfFilePath.lastIndexOf("/")+1,pdfFilePath.lastIndexOf("."));
//        String dstImgFolder="img";
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

            if (createDirectory(imgFolderPath)) {
                String imgPdfPath = file.getParent();
                pdDocument = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                PdfReader reader = new PdfReader(pdfFilePath);
                //pdf页数
                int pages = reader.getNumberOfPages();
                reader.close();
                StringBuilder imgFilePath;
                for (int i = 0; i < pages; i++) {
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
                return fileList;
            } else {
                System.out.println("PDF文档转PNG图片失败：" + "创建" + imgFolderPath + "失败");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    //创建文件夹
    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }
    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();

            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
