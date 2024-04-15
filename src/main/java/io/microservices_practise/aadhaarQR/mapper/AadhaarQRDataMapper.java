package io.microservices_practise.aadhaarQR.mapper;

import io.microservices_practise.aadhaarQR.dto.AadhaarQRDataDTO;
import io.microservices_practise.aadhaarQR.entity.AadhaarQRData;
import org.springframework.stereotype.Service;

public interface AadhaarQRDataMapper {
    AadhaarQRData aadhaarQRDataDTOToEntity(AadhaarQRDataDTO aadhaarQRDataDTO);
}
