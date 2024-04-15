package io.microservices_practise.aadhaarQR.service.impl;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import io.microservices_practise.aadhaarQR.service.CompressBase64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Service
public class CompressBase64Impl implements CompressBase64 {
    @Override
    public String compressBase64String(String base64Img) {
        try {
            byte[] imageInBytes = base64Img.getBytes();

            log.info("Compressing base 64 image");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
                gzipOS.write(imageInBytes);
            }

            byte[] compressedImageInBytes = bos.toByteArray();
            String compressedImage = Base64.getEncoder().encodeToString(compressedImageInBytes);

            log.info("base 64 image org length : {}", base64Img.length());
            log.info("base 64 image compressed length : {}", compressedImage.length());

            return compressedImage;
        } catch (Exception e) {
            log.error("Error occured while compressing base 64 image string");
            throw new RuntimeException("Error occured while compressing base 64 image string");
        }
    }

    @Override
    public String decompressBase64String(String compressedImg) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedImg.getBytes());
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream gzipIS = new GZIPInputStream(bis)) {

            byte[] buffer = new byte[1024];
            int len;

            log.info("Decompressing compressed image string");
            while ((len = gzipIS.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }

            return Arrays.toString(bos.toByteArray());
        } catch (Exception e) {
            log.error("Error occured while decompressing compressed image string");
            throw new RuntimeException("Error occured while decompressing compressed image string");
        }
    }

    public boolean isDataTooBig(String data, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        log.info("data  length : {}", data.length());
        QRCode qrCode = Encoder.encode(data, errorCorrectionLevel, null);
        log.info("**************************************************************************************");
        log.info("qrCode matrix height : {} , width : {}", qrCode.getMatrix().getWidth(), qrCode.getMatrix().getHeight());
        log.info("max Capacity : {}" , qrCode.getMatrix().getWidth() * qrCode.getMatrix().getHeight());
        return data.length() > qrCode.getMatrix().getWidth() * qrCode.getMatrix().getHeight();
    }
}
