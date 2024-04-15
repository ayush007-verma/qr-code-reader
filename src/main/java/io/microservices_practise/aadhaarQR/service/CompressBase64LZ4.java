package io.microservices_practise.aadhaarQR.service;

public interface CompressBase64LZ4 {

    byte[] compressBase64(byte[] dataInBytes);
    byte[] decompressBase64(byte[] dataInBytes, int orgLength);
}
