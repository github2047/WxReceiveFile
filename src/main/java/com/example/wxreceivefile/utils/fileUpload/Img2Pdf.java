package com.example.wxreceivefile.utils.fileUpload;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Img2Pdf {
    /**
     *
     * @param files MultipartFile类型文件
     * @param plotScale 比例，原图大小/自适应 1/2
     * @param paperFx   纸张方向 横向/纵向
     * @return MultipartFile类型文件
     * @throws IOException
     * @throws DocumentException
     */
    public static MultipartFile
    toPdf(MultipartFile files[], String plotScale, String paperFx) throws IOException, DocumentException {

        String pdfFileName = "图片.pdf";
        Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
        FileOutputStream fileOutputStream=new FileOutputStream(pdfFileName);
        PdfWriter.getInstance(doc, fileOutputStream);
        doc.open();
        for (MultipartFile file : files) {
            doc.newPage();
            Image image = Image.getInstance(file.getBytes());
            float height = image.getHeight();
            float width = image.getWidth();
            int percent = getPercent(height, width, paperFx);
            image.setAlignment(Image.MIDDLE);
            if (paperFx.equals("1")) {
                image.setRotation((float) (Math.PI / 2 * 3));
            }
            //原始大小
            if (plotScale.equals("1")) {
                image.scaleAbsolute(width, height);
            }
            //自适应大小
            else if (plotScale.equals("2")) {
                image.scalePercent(percent);
            }
            //身份证选项，百分百
            else if (plotScale.equals("3")) {
                int percentCard = getPercentCard(height, width);
                image.scalePercent(percentCard);
            }
            doc.add(image);
        }
        doc.close();
        fileOutputStream.close();
        File file1 = new File(pdfFileName);
        FileInputStream fileInputStream=new FileInputStream(file1);
        MultipartFile multipartFile = new MockMultipartFile(pdfFileName, pdfFileName, "application/pdf", fileInputStream);
        fileInputStream.close();
        return multipartFile;
    }

    /**
     * 等比压缩，获取压缩百分比
     *
     * @param height 图片的高度
     * @param weight 图片的宽度
     *               身份证 自适应 等大小
     * @return 压缩百分比
     */
    private static int getPercent(float height, float weight, String rotation) {
        float percent = 0.0F;
        if (rotation.equals("2")) {
            if (height > weight) {
                percent = PageSize.A4.getHeight() / height * 90;
            } else {
                percent = PageSize.A4.getWidth() / weight * 90;
            }
            return Math.round(percent);
        } else if (rotation.equals("1")) {
            if (PageSize.A4.getHeight() / weight < PageSize.A4.getWidth() / height) {
                percent = PageSize.A4.getHeight() / weight * 95;
            } else {
                percent = PageSize.A4.getWidth() / height * 95;
            }
            return Math.round(percent);
        }
        return 0;
    }

    private static int getPercentCard(float height, float weight) {
        float percent = 0.0F;
        if (height > weight) {
            percent = PageSize.A4.getHeight() / height * 100;
        } else {
            percent = PageSize.A4.getWidth() / weight * 100;
        }
        return Math.round(percent);
    }
}
