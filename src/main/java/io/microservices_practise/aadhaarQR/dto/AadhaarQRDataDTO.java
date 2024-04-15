package io.microservices_practise.aadhaarQR.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AadhaarQRDataDTO {
    private String name;

    private String email;

    private String mobNo;

    private String base64Img;
}
