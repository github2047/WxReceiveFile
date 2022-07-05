package com.example.wxreceivefile.controller;

import java.io.*;
import java.util.UUID;

import com.aspose.cells.*;      //引入aspose-cells-8.5.2.jar包
import javafx.scene.text.TextAlignment;
import org.apache.ibatis.io.ResolverUtil;
import org.omg.PortableServer.THREAD_POLICY_ID;

public class Excel2Pdf {
    public static boolean getLicense() {
         boolean result = false;
         try {
             InputStream is = ResolverUtil.Test.class.getClassLoader().getResourceAsStream("license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
             License aposeLic = new License();
             aposeLic.setLicense(is);
             result = true;
         } catch (Exception e) {
             e.printStackTrace();
         }
         return result;
     }

     public static void excel2pdf(String Address) {

         if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
             return;
         }
         try {
             File pdfFile = new File("D:/upload/"+ UUID.randomUUID() +".pdf");// 输出路径
             Workbook wb = new Workbook(Address);// 原始excel路径
             FileOutputStream fileOS = new FileOutputStream(pdfFile);


             PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
             //显示在同一页
             pdfSaveOptions.setOnePagePerSheet(true);
             int[] autoDrawSheets={};
             //当excel中对应的sheet页宽度太大时，在PDF中会拆断并分页。此处等比缩放。
            autoDraw(wb,autoDrawSheets);

            //sheet数组
             int[] showSheets={0,1,2};
             //隐藏workbook中不需要的sheet页。
             printSheetPage(wb,showSheets);
            //设置边框
             SetWorkbookStyle(wb);

             wb.save(fileOS, pdfSaveOptions);
             fileOS.flush();
             fileOS.close();
             System.out.println("完毕");
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    /**
     * 设置打印的sheet 自动拉伸比例
     * @param wb
     * @param page 自动拉伸的页的sheet数组
     */
    public static void autoDraw(Workbook wb,int[] page){
        if(null!=page&&page.length>0){
            for (int i = 0; i < page.length; i++) {
                wb.getWorksheets().get(i).getHorizontalPageBreaks().clear();
                wb.getWorksheets().get(i).getVerticalPageBreaks().clear();
            }
        }
    }


    /**
     * 隐藏workbook中不需要的sheet页。
     * @param wb
     * @param page 显示页的sheet数组
     */
    public static void printSheetPage(Workbook wb,int[] page){
        for (int i= 1; i < wb.getWorksheets().getCount(); i++)  {
            wb.getWorksheets().get(i).setVisible(false);
        }
        if(null==page||page.length==0){
            wb.getWorksheets().get(0).setVisible(true);
        }else{
            for (int i = 0; i < page.length; i++) {
                wb.getWorksheets().get(i).setVisible(true);
            }
        }
    }
    //设置excel边框样式
    private static void SetWorkbookStyle(Workbook wb) {
        WorksheetCollection worksheets = wb.getWorksheets();      //获取sheet集合

        int count = worksheets.getCount();
        Style style = wb.createStyle();
        style.setBorder(BorderType.TOP_BORDER,CellBorderType.HAIR, Color.getBlack());
        style.setBorder(BorderType.LEFT_BORDER,CellBorderType.HAIR, Color.getBlack());
        style.setBorder(BorderType.BOTTOM_BORDER,CellBorderType.HAIR, Color.getBlack());
        style.setBorder(BorderType.RIGHT_BORDER,CellBorderType.HAIR, Color.getBlack());
        //文字居中
//        style.setHorizontalAlignment(TextAlignmentType.CENTER);

        for (int i = 0; i < count; i++)     //每一sheet
        {
            //worksheets[i].Cells.SetColumnWidth(1, 30);//设置列宽
            System.out.println(worksheets.get(i).getCells().getMaxRow());
            System.out.println(worksheets.get(i).getCells().getMaxColumn());
            int count1 = worksheets.get(i).getCells().getCount();
            for (int j = 0; j < count1; j++)  //sheet中的每一个单元格都采用这种风格
            {
                Cell hg = worksheets.get(i).getCells().get(j);
                hg.setStyle(style);
            }

        }
    }
    public static void main(String[] args) {
        Excel2Pdf.excel2pdf("D:/upload/1.xls");
    }
 }
