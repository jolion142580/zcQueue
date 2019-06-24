package com.yiko.common.utils.poi.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.yiko.common.service.File2PdfService;
import com.yiko.common.service.impl.Doc2PdfServiceImpl;
import org.springframework.stereotype.Component;


@Component
public class PreviewUtils {
    private static File2PdfService fileConvertService;

    //private static LocalFilePathManage localFilePathManage;


//	 public static PreviewUtils previewUtils;
//	 
//	 @PostConstruct
//	 public void init(){
//		 previewUtils = this;
//	 }

	 
/*	public static LocalFilePathManage getLocalFilePathManage() {
        return localFilePathManage;
	}


	public static void setLocalFilePathManage(LocalFilePathManage localFilePathManage) {
		PreviewUtils.localFilePathManage = localFilePathManage;
	}*/

    public static void main(String[] args) throws InterruptedException, IOException {
       /* String officePath = "D:/test/SB5090《佛山市基本医疗保险参保人员长驻（住）异地就医申请表》（广东省内适用）_c23e6a15-f012-4324-8d0b-2a405d2511fa.docx";
        String savePdfPath = "D:\\test\\test2/test.pdf";
        try {
            PreviewUtils.officeConversionPDF(officePath,savePdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

      /*  Process p = Runtime.getRuntime()
                .exec("cmd.exe /c " + " " + wordToHtmPath + " " + wordFilePath + " " + pdfFilePath);
        p.waitFor();
        p.destroy();*/
      /*  String wordToHtmPath = "C:\\wordToPdf.vbs";
        String wordFilePath = "D:\\test/1.docx";
        String pdfFilePath = "D:\\test\\test2/test2.pdf";
        Process p = Runtime.getRuntime()
                .exec("cmd.exe /c " + " " + wordToHtmPath + " " + wordFilePath + " " + pdfFilePath);
        p.waitFor();
        p.destroy();*/


    }


    /**
     * office转换PDF
     *
     * @return
     * @throws Exception
     */
    public static int officeConversionPDF(String officePath, String savePdfPath) {
        //获取文件后缀
//	        String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        //String fileNames = fileName.substring(0,fileName.lastIndexOf("."));

//        fileConvertService = new Doc2PdfServiceImpl();
        //根据后缀判断文件类型实例化对应服务层
           /* if (StringUtils.equalsIgnoreCase(suffix,"docx") || StringUtils.equalsIgnoreCase(suffix,"doc")){
                fileConvertService = new Doc2PdfServiceImpl();
	        }else if (StringUtils.equalsIgnoreCase(suffix,"pptx") || StringUtils.equalsIgnoreCase(suffix,"ppt")){
	            fileConvertService = new PPT2PdfServiceImpl();
	        }else if (StringUtils.equalsIgnoreCase(suffix,"xlsx") || StringUtils.equalsIgnoreCase(suffix,"xls")){
	            fileConvertService = new Excel2PdfServiceImpl();
	        }*/
//        int convertStatus = 0;
        //获取需要转换文档的所在路径savePath()+File.separator+fileName
//        File officeFile = new File(officePath);//"D:\\develop\\zcwx\\"+fileName
//        File outputFile = new File(savePdfPath);//PDF保存文件路径 "D:\\develop\\zcwx\\" +File.separator+ fileNames+".pdf"
        //File officeFile = new File(savePath()+File.separator+fileName);
        //File outputFile = new File(outPDFPath() +File.separator+ fileNames+".pdf");//PDF保存文件路径
        //如果文件是 PDF 不用转换直接复制到指定目录
            /*if (StringUtils.equalsIgnoreCase(suffix,"pdf")){
                FileRWUtils.copyFile(officeFile.getPath(),outPDFPath(fileNames));
	            return convertStatus;
	        }*/
      /*  if (!outputFile.exists()) {
            InputStream inputStream = new FileInputStream(officeFile);
            OutputStream outputStream = new FileOutputStream(outputFile);
            //开始转换
            convertStatus = fileConvertService.convert2Pdf(inputStream, outputStream);
        }
        return convertStatus;*/

        try {
            String wordToHtmPath = "C:\\wordToPdf.vbs";

            Process p = Runtime.getRuntime()
                    .exec("cmd.exe /c " + " " + wordToHtmPath + " " + officePath + " " + savePdfPath);
            p.waitFor();
            p.destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;

    }


    /**
     * 根据PDF文件名获取PDF存储地址
     *
     * @return 没有加.toURI() 有可能中文路径会乱码 加上获取会出错
     */
    public static String outPDFPath() throws Exception {
          /*  String pathName = PreviewUtils.class.getClassLoader().getResource("").getPath();
	        //文件保存位置 如果文件夹不存在创建文件夹
	        File saveDir = new File(pathName+File.separator+"outputFile");
	        if(!saveDir.exists()){
	            saveDir.mkdir();
	        }*/
			/*String path = localFilePathManage.getLocalFilePath()+
					localFilePathManage.getTemplatePath();

	        return path;*/
        return "";
    }

    /**
     * 获取文件保存路径
     * @return public static String savePath(){

    String path = localFilePathManage.getLocalFilePath()+
    localFilePathManage.getTemplatePath();

    return path;
    }*/

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr   下载地址
     * @param fileName 文件名
     * @param savePath 保存地址
     * @throws IOException
     */
    public static boolean downLoadFromUrl(String urlStr, String fileName, String savePath) {
        File file = new File(savePath + File.separator + fileName);
        //文件是否已存在
        if (file.exists()) {
            return true;
        }
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为30秒
            conn.setConnectTimeout(30 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            FileOutputStream fos = new FileOutputStream(file);
            if (getData == null) {
                return false;
            }
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException e) {
//	            LogUtil.error("附件下载转换PDF出错!",e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


}
