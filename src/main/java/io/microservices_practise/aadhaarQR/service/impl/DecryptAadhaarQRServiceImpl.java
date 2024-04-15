package io.microservices_practise.aadhaarQR.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.microservices_practise.aadhaarQR.dto.AadhaarQRDataDTO;
import io.microservices_practise.aadhaarQR.entity.AadhaarQRData;
import io.microservices_practise.aadhaarQR.mapper.AadhaarQRDataMapper;
import io.microservices_practise.aadhaarQR.repository.AadhaarQRDataRepository;
import io.microservices_practise.aadhaarQR.service.CompressBase64;
import io.microservices_practise.aadhaarQR.service.DecryptAadhaarQRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
@Service
public class DecryptAadhaarQRServiceImpl implements DecryptAadhaarQRService {
    @Autowired
    AadhaarQRDataRepository aadhaarQRDataRepository;

    @Autowired
    AadhaarQRDataMapper aadhaarQRDataMapper;

    @Autowired
    CompressBase64 compressBase64;

    @Override
    public void handleAadhaarQR(String aadhaarQr) {
        log.info("handle aadhaar QR");
    }

    @Override
    public AadhaarQRData saveAadhaarDetails(AadhaarQRDataDTO aadhaarDTO) {
        log.info("Saving Aadhaar QR data");

        if (aadhaarQRDataRepository.findByName(aadhaarDTO.getName()).isPresent()) {
            log.error("Aadhaar Data with same credentials already presnt {}", aadhaarDTO);
            throw new RuntimeException("Credentials already exists for this Aadhaar Data");
        }

        try {
            return aadhaarQRDataRepository
                    .saveAndFlush(aadhaarQRDataMapper
                            .aadhaarQRDataDTOToEntity(aadhaarDTO)
                            .setBase64ImgPath(saveBase64ImgLocally(aadhaarDTO.getBase64Img(), aadhaarDTO.getName()))
                            .setAadhaarQrCodePath(createQRForAadhaarData(aadhaarDTO)));
        } catch (Exception e) {
            throw new RuntimeException("Not able to save aadhaar Details : " + e.getMessage());
        }
    }

    private String saveBase64ImgLocally(String base64Img, String name) {
        String filePath = "files/base64Images/" + name.replace(' ', '_') + ".jpg";
        try {
            byte[] imageInBytes = Base64.getDecoder().decode(base64Img);

            log.info("Creating File for base 64 img : {}", filePath);
            File file = createFileAtPath(filePath);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(imageInBytes);
            }

            log.info("Saved image successfully File Location : {}", file.getParentFile().getAbsolutePath());
            return file.getParentFile().getAbsolutePath();
        } catch (Exception e) {
            log.error("Error while saving base 64 image locally at path : {}", filePath);
            log.error("Error : {}", e.getMessage());
        }
        return null;
    }

    private File createFileAtPath(String filePath) {
        File file = new File(filePath);

        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            log.error("Failed to create a new directory at path : {}", filePath);
            throw new RuntimeException("Failed to create a new directory at path : {}" + filePath);
        }
        log.info("Created dir at path : {}", filePath);

        return file;
    }

    private String createQRForAadhaarData(AadhaarQRDataDTO aadhaarData) throws WriterException, IOException {
        aadhaarData.setBase64Img(compressBase64.compressBase64String(aadhaarData.getBase64Img()));
        String aadhaarDataJson = new ObjectMapper().writeValueAsString(aadhaarData);
        log.info("Aadhaar Data json length : {}", aadhaarDataJson.length());

        String filePath = "files/aadhaarQR/" + aadhaarData.getName().replace(' ', '_') + ".jpg";
        String charset = "UTF-8";
        int height = 200;
        int width = 200;

        if (compressBase64.isDataTooBig(aadhaarDataJson, ErrorCorrectionLevel.H)) {
            throw new IllegalArgumentException("Data too big to be stored in QR Code");
        }

        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(new String(aadhaarDataJson.getBytes(charset), charset)
                            , BarcodeFormat.QR_CODE, width, height);
            log.info("Creating File for aadhaar qr : {}", filePath);
            File file = createFileAtPath(filePath);

            MatrixToImageWriter.writeToPath(matrix,
                    filePath.substring(filePath.lastIndexOf('.') + 1),
                    file.toPath());
            log.info("Saved QR Code in file at path : {}", file.getParentFile().getAbsolutePath());
            return file.getParentFile().getAbsolutePath();
        } catch (Exception e) {
            log.error("Error while saving qr : {}", e.getMessage());
            throw new RuntimeException("Error while saving aadhaar qr" + e.getMessage());
        }
    }

    public void testBitMatrix(String json, int width, int height) throws UnsupportedEncodingException, WriterException {
        String filePath = "files/aadhaarQR/" + "test" + ".jpg";
        String charset = "UTF-8";
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(new String(json.getBytes(charset), charset)
                            , BarcodeFormat.QR_CODE, width, height);
            log.info("Creating File for aadhaar qr : {}", filePath);
            File file = createFileAtPath(filePath);

            MatrixToImageWriter.writeToPath(matrix,
                    filePath.substring(filePath.lastIndexOf('.') + 1),
                    file.toPath());
            log.info("Saved QR Code in file at path : {}", file.getParentFile().getAbsolutePath());
        } catch (Exception e) {
            log.error("Error while saving qr : {}", e.getMessage());
            throw new RuntimeException("Error while saving aadhaar qr" + e.getMessage());
        }
    }
}
