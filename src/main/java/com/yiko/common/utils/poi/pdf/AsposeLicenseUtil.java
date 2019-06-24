package com.yiko.common.utils.poi.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

public class AsposeLicenseUtil {

    private static InputStream inputStream = null;

    private static Logger logger = Logger.getLogger(AsposeLicenseUtil.class);


    /**
     * 获取License的输入流
     *
     * @return
     */
    private static InputStream getLicenseInput() {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {

            inputStream = AsposeLicenseUtil.class.getClassLoader().getResourceAsStream("config/license.xml");
            //System.out.println("===========================inputStream=================================="+inputStream);
        } catch (Exception e) {
            logger.error("license not found!", e);
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 设置License
     *
     * @return true表示已成功设置License, false表示失败
     */
    public static boolean setWordsLicense() {
        InputStream licenseInput = getLicenseInput();
        if (licenseInput != null) {
            try {
                com.aspose.words.License aposeLic = new com.aspose.words.License();
                aposeLic.setLicense(licenseInput);
                return aposeLic.getIsLicensed();
            } catch (Exception e) {
                logger.error("set words license error!", e);
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 设置License
     *
     * @return true表示已成功设置License, false表示失败
     */
/*    public static boolean setCellsLicense() {
        InputStream licenseInput = getLicenseInput();
        if (licenseInput != null) {
            try {
                com.aspose.cells.License aposeLic = new com.aspose.cells.License();
                aposeLic.setLicense(licenseInput);
                return true;
            } catch (Exception e) {
                logger.error("set cells license error!", e);
                e.printStackTrace();
            }
        }
        return false;
    }*/

    /**
     * 设置License
     *
     * @return true表示已成功设置License, false表示失败
     */
/*    public static boolean setSlidesLicense() {
        InputStream licenseInput = getLicenseInput();
        if (licenseInput != null) {
            try {
                com.aspose.slides.License aposeLic = new com.aspose.slides.License();
                aposeLic.setLicense(licenseInput);
                return aposeLic.isLicensed();
            } catch (Exception e) {
                logger.error("set ppt license error!", e);
                e.printStackTrace();
            }
        }
        return false;
    }*/

    /**
     * 设置Aspose PDF的license
     * @return true表示设置成功，false表示设置失败
     */
/*    public static boolean setPdfLicense() {
        InputStream licenseInput = getLicenseInput();
        if (licenseInput != null) {
            try {
                com.aspose.pdf.License aposeLic = new com.aspose.pdf.License();
                aposeLic.setLicense(licenseInput);
                return true;
            } catch (Exception e) {
                logger.error("set pdf license error!", e);
                e.printStackTrace();
            }
        }
        return false;
    }*/
}