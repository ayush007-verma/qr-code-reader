package io.microservices_practise.aadhaarQR.repository;

import io.microservices_practise.aadhaarQR.entity.AadhaarQRData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AadhaarQRDataRepository extends JpaRepository<AadhaarQRData, Long> {
    Optional<AadhaarQRData> findByName(String name);
}
