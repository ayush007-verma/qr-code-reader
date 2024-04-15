package io.microservices_practise.aadhaarQR.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity(name = "aadhaar_data")
@Getter
@Setter
@Accessors(chain = true)
public class AadhaarQRData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Column(name = "mobile_number")
    private String mobNo;

    @Column(name = "image_path")
    private String base64ImgPath;

    @Column(name = "qr_code_path")
    private String aadhaarQrCodePath;
}
