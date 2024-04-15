package io.microservices_practise.aadhaarQR.service;

import com.google.zxing.WriterException;
import io.microservices_practise.aadhaarQR.dto.AadhaarQRDataDTO;
import io.microservices_practise.aadhaarQR.entity.AadhaarQRData;

import java.io.UnsupportedEncodingException;


public interface DecryptAadhaarQRService {
    void handleAadhaarQR(String aadhaarQr);
    AadhaarQRData saveAadhaarDetails(AadhaarQRDataDTO aadhaarDTO);

    public void testBitMatrix(String json, int height, int width) throws UnsupportedEncodingException, WriterException;
}
