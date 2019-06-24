package com.yiko.common.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface File2PdfService {

    public int convert2Pdf(InputStream inputStream, OutputStream outputStream);
}
