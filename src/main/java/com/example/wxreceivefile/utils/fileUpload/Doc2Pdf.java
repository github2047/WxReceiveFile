package com.example.wxreceivefile.utils.fileUpload;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Doc2Pdf {
    public  boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("lic/aspose.cells.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  void  toPdf(MultipartFile file, String outPath) throws Exception {
        if (!getLicense()) {
            System.out.println("非法------------");
            return;
        }
        long old = System.currentTimeMillis();

        File file1 = new File(outPath);
        FileOutputStream os = new FileOutputStream(file1);

        //MultipartFile转inputStream
        byte[] byteArr = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArr);
        Document doc = new Document(inputStream);
        inputStream.close();

        //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
        doc.save(os, SaveFormat.PDF);
        os.close();
        long now = System.currentTimeMillis();
        System.out.println("文件转PDF耗时：" + ((now - old) / 1000.0) + "秒");
    }
}
