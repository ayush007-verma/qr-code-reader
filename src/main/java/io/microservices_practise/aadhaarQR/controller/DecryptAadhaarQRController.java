package io.microservices_practise.aadhaarQR.controller;

import com.google.zxing.WriterException;
import com.google.zxing.pdf417.decoder.ec.ErrorCorrection;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.microservices_practise.aadhaarQR.dto.AadhaarQRDataDTO;
import io.microservices_practise.aadhaarQR.entity.AadhaarQRData;
import io.microservices_practise.aadhaarQR.service.CompressBase64;
import io.microservices_practise.aadhaarQR.service.CompressBase64LZ4;
import io.microservices_practise.aadhaarQR.service.DecryptAadhaarQRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/aadhaar")
public class DecryptAadhaarQRController {

    @Autowired
    DecryptAadhaarQRService decryptAadhaarQRService;

    @Autowired
    CompressBase64 compressBase64;

    @Autowired
    CompressBase64LZ4 compressBase64LZ4;

    @GetMapping("/decrypt")
    public void decryptAadhaarQr() {
        log.info("Request to decrypt aadhaar QR ");
    }

    @PostMapping("/save")
    public ResponseEntity<String> storeAadhaarDetails(@RequestBody AadhaarQRDataDTO aadhaarDTO) {
        AadhaarQRData savedAadhaarData;
        try {
            savedAadhaarData = decryptAadhaarQRService.saveAadhaarDetails(aadhaarDTO);
        } catch (Exception e) {
            log.error("Error while saving aadhaar Details : {}", e.getMessage());
            return new ResponseEntity<>("Failed to store aadhaar Details : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(savedAadhaarData.getName() + " successfully saved ", HttpStatus.ACCEPTED);
    }

    @PostMapping("/validate/qr/v1")
    public ResponseEntity<String> validateQRInputData(@RequestBody String inputData, @RequestParam int splitIndex) throws WriterException {
        try {
            String compressedImageString = compressBase64.compressBase64String(inputData);
            log.info("compressedImageString length : {}", compressedImageString.length());
//            compressedImageString = compressedImageString.substring(splitIndex);
            log.info("compressedImageString substring : {}  length : {}", splitIndex, compressedImageString.length());
            log.info("**************************************************************************************");
            return new ResponseEntity<>(compressBase64.isDataTooBig(compressedImageString, ErrorCorrectionLevel.H) ? "Data too Big to be stored in qr Code" : "Data feasible enough to be stored in qr", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to validate qr : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/validate/qr/v2")
    public ResponseEntity<String> validateQRInputDataV2(@RequestBody AadhaarQRDataDTO aadhaarDTO,
                                                        @RequestParam(required = false) int splitIndex,
                                                        @RequestParam(required = false) int height,
                                                        @RequestParam(required = false) int width) throws WriterException {
        try {
            String compressedImageString = Base64.getEncoder().encodeToString(compressBase64LZ4.compressBase64(aadhaarDTO.getBase64Img().getBytes()));
            log.info("compressedImageString length : {}", compressedImageString.length());

            decryptAadhaarQRService.testBitMatrix(compressedImageString, height, width);
            return new ResponseEntity<>(" successfully validated ", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to validate qr : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
