package com.yiko.common.utils;



import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hubiao
 * @dateTime 2014-06-07
 * 本工具对实现对IMG与PDF相互转换。
 * 运行测试需要导入以下2个jar包
 * itext-2.0.2.jar
 * PDFRenderer.jar
 */
@SuppressWarnings("unused")
public class ImgPdfUtils {

    public static void main(String[] args) throws Exception {
        //测试多个图片和合并为pdf文件
        File file1 = new File("D:/develop/1.jpg");
        File file2 = new File("D:/develop/2.jpg");
        List<File> list = new ArrayList<File>();
        list.add(file1);
        list.add(file2);
        ImgPdfUtils.imgMerageToPdf(list, new File("D:/develop/test.pdf"));

        //测试将pdf文件中指定的页码的页面转换为图片
       /* ImgPdfUtils.pdfToJpg("D:/workspaces/InCoOSDocking/test/picture/test.pdf",
				"D:/workspaces/InCoOSDocking/test/picture/test.jpg", 1);*/
		
		//测试pdf提取
		/*ImgPdfUtils.pdfExtraction("D:/workspaces/InCoOSDocking/test/picture/test.pdf",
				"D:/workspaces/InCoOSDocking/test/picture/test2.pdf", 1);

        //将一个jpg图片转换为pdf文件
		File file1 = new File("D:/workspaces/InCoOSDocking/test/picture/1.jpg");
		File file2 = new File("D:/workspaces/InCoOSDocking/test/picture/test2.pdf");
		ImgPdfUtils.jpgToPdf(file1, file2);*/
    }


    /**
     * 将图片文件对象集合中的图片按照集合中的顺序合并为pdf文件,每一个图片都是pdf文件中的一页
     *
     * @throws 如果合并中产生错误,将抛出异常
     * @param    list    图片文件对象集合
     * @param    file 合并后的pdf文件的保存路径,必须包含pdf文件名称
     * @return true, 合并完成
     */
    public static boolean imgMerageToPdf(List<File> list, File file) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048 * 3);
        InputStream is = new FileInputStream(list.get(1));
        for (int len; (len = is.read()) != -1; ) {
            baos.write(len);
        }
        baos.flush();
        Image image = Image.getInstance(baos.toByteArray());
        float width = image.getWidth();
        float height = image.getHeight();

        baos.close();

        //2:通过宽高 ，实例化PDF文档对象。
        Document document = new Document(new Rectangle(width, height));
        PdfWriter pdfWr = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        //3：获取每一个图片文件，转为IMG对象。装载到Document对象中
        for (File imgFile : list) {
            //4.1:读取到内存中
            baos = new ByteArrayOutputStream(2048 * 3);
            is = new FileInputStream(imgFile);
            for (int len; (len = is.read()) != -1; ) {
                baos.write(len);
            }
            baos.flush();

            //4.2通过byte字节生成IMG对象
            image = Image.getInstance(baos.toByteArray());
//            image.setAbsolutePosition(0.0f, 0.0f);
            //统一按照宽度压缩
            Integer percent = getPercent(height, width);
//        //设置图片居中显示
            image.setAlignment(Image.MIDDLE);
//        //按百分比显示图片的比例
            image.scalePercent(percent);
//        //可设置图像高和宽的比例
            image.scalePercent(80, 80);
            //4.3：添加到document中,然后打开下一页
            document.add(image);
            document.newPage();
            baos.close();
        }

        //5：释放资源
        is.close();
        document.close();
        pdfWr.close();

        return true;
    }


    /**
     * pdf转图片
     * <p>经测试转换后图片的类型可以为jpg、jpeg
     *
     * @param source 源文件路径
     * @param target 目标文件路径
     * @param x      读取源文件中的第几页
     */
    private static void pdfToJpg(String source, String target, int x) throws Exception {
        //1:创建从中读取和向其中写入（可选）的随机访问文件流，R表示对其只是访问模式
        RandomAccessFile rea = new RandomAccessFile(new File(source), "r");

        //2:将流读取到内存中，然后还映射一个PDF对象
        FileChannel channel = rea.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdfFile = new PDFFile(buf);
        PDFPage page = pdfFile.getPage(x);

        //3:创建一个区域对象,其中高度和宽度根据pdf文件中指定的页面的大小确定
        // get the width and height for the doc at the default zoom
        java.awt.Rectangle rect = new java.awt.Rectangle(0, 0, (int) page.getBBox()
                .getWidth(), (int) page.getBBox().getHeight());

        //4:根据区域对象创建图片对象
        // generate the image  
        java.awt.Image img = page.getImage(rect.width, rect.height, // width &
                rect, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true // block until drawing is done
        );

        //5:创建有一个图像缓冲区,并且根据图像对象进行绘制
        BufferedImage tag = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(img, 0, 0, rect.width, rect.height, null);

        //6:创建图片文件并设置其内容
        FileOutputStream out = new FileOutputStream(target); // 输出到文件流
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(tag); // JPEG编码

        out.close();
        rea.close();
    }

    /**
     * PDF包提取 pdf
     *
     * @param source  源PDF文件路径
     * @param target  保存PDF文件路径
     * @param pageNum 提取PDF中第pageNum页
     * @throws Exception
     */
    private static void pdfExtraction(String source, String target, int pageNum) throws Exception {
        //1：创建PDF读取对象
        PdfReader pr = new PdfReader(source);
        System.out.println("this document " + pr.getNumberOfPages() + " page");

        //2：将第page页转为提取，创建document对象
        Document doc = new Document(pr.getPageSize(pageNum));

        //3：通过PdfCopy转其单独存储
        PdfCopy copy = new PdfCopy(doc, new FileOutputStream(new File(target)));
        doc.open();
        doc.newPage();

        //4：获取第1页，装载到document中。
        PdfImportedPage page = copy.getImportedPage(pr, pageNum);
        copy.addPage(page);

        //5：释放资源
        copy.close();
        doc.close();
        pr.close();
    }

    /**
     * 将一个jpg图片转pdf
     *
     * @param imgFile 图片文件
     * @param pdfFile PDF文件
     */
    private static void jpgToPdf(File imgFile, File pdfFile) throws Exception {
        //1:读取图片
        InputStream is = new FileInputStream(imgFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i; (i = is.read()) != -1; ) {
            baos.write(i);
        }
        baos.flush();

        //2:取得图像的宽和高。
        Image img = Image.getInstance(baos.toByteArray());
        float width = img.getWidth();
        float height = img.getHeight();
        img.setAbsolutePosition(0.0F, 0.0F);//取消偏移
        System.out.println("width = " + width + "\theight" + height);

        //img转pdf
        Document doc = new Document(new Rectangle(width, height));
        PdfWriter pw = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
        doc.open();
        doc.add(img);

        //释放资源
        System.out.println(doc.newPage());
        pw.flush();
        is.close();
        baos.close();
        doc.close();
        pw.close();
    }

    /**
     * 按照宽度压缩
     * @param
     */
    public static Integer getPercent(Float h,Float w)
    {
        Integer p=0;
        Float p2=0.0f;
        p2=530/w*100;
        System.out.println("--"+p2);
        p=Math.round(p2);
        return p;
    }




}
