package io.microservices_practise.aadhaarQR.service;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public interface CompressBase64 {
    String compressBase64String(String base64Img);

    String decompressBase64String(String base64Img);

    boolean isDataTooBig(String data, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException;
}
