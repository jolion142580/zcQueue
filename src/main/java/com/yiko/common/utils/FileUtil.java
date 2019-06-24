package com.yiko.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 加载本地文件,并转换为byte数组
     *
     * @return
     */
    public static byte[] loadFile(File file) {

        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] data = null;

        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream((int) file.length());

            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    //是否已这些类型为结尾
    public static boolean isSuccessFileName(String fileName) {
        if (null != fileName && fileName.lastIndexOf(".") != -1) {
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (StringUtils.isNotBlank(ext)) {
                if (!ext.equals("doc") && !ext.equals("docx") && !ext.equals("xls")) {
                    return false;
                }

            }
        }
        return true;
    }
}
