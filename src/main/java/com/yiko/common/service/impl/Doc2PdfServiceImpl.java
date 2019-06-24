package com.yiko.common.service.impl;

import com.aspose.words.Document;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;
import com.yiko.common.service.File2PdfService;
import com.yiko.common.utils.poi.pdf.AsposeLicenseUtil;

import java.io.InputStream;
import java.io.OutputStream;



public class Doc2PdfServiceImpl implements File2PdfService {

    @Override
    public int convert2Pdf(InputStream inputStream, OutputStream outputStream) {
        try {
            if (AsposeLicenseUtil.setWordsLicense()) {

                Document doc = new Document(inputStream);

//                insertWatermarkText(doc, "水印水印"); // 添加水印

                PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
                pdfSaveOptions.setSaveFormat(SaveFormat.PDF);
            /*    pdfSaveOptions.getOutlineOptions().setHeadingsOutlineLevels(1); // 设置3级doc书签需要保存到pdf的heading中
                pdfSaveOptions.getOutlineOptions().setExpandedOutlineLevels(1);*/ // 设置pdf中默认展开1级

                doc.save(outputStream, pdfSaveOptions);
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 3;
        }
    }


/*	public static void main(String[] args) {
        File officeFile = new File("D:\\2.doc");
        File outputFile = new File("D:\\2.pdf");//PDF保存文件路径
        InputStream inputStream;
        OutputStream outputStream;
		try {
			inputStream = new FileInputStream(officeFile);
			 outputStream = new FileOutputStream(outputFile);
			 File2PdfService file2pdf = new Doc2PdfServiceImpl();
			int i =	file2pdf.convert2Pdf(inputStream, outputStream);
			System.out.println(i);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
		
	}*/

}
