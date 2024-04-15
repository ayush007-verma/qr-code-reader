package io.microservices_practise.aadhaarQR.mapper.impl;

import io.microservices_practise.aadhaarQR.dto.AadhaarQRDataDTO;
import io.microservices_practise.aadhaarQR.entity.AadhaarQRData;
import io.microservices_practise.aadhaarQR.mapper.AadhaarQRDataMapper;
import org.springframework.stereotype.Service;

@Service
public class AadhaarQRDataMapperImpl implements AadhaarQRDataMapper {
    public AadhaarQRData aadhaarQRDataDTOToEntity(AadhaarQRDataDTO aadhaarQRDataDTO) {
        return new AadhaarQRData()
                .setName(aadhaarQRDataDTO.getName())
                .setEmail(aadhaarQRDataDTO.getEmail())
                .setMobNo(aadhaarQRDataDTO.getMobNo());
    }
}