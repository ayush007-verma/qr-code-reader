package io.microservices_practise.aadhaarQR.service.impl;

import io.microservices_practise.aadhaarQR.service.CompressBase64LZ4;
import lombok.extern.slf4j.Slf4j;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class CompressBase64LZ4Impl implements CompressBase64LZ4 {

    public byte[] compressBase64(byte[] dataInBytes) {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4Compressor compressor = factory.fastCompressor();
        int maxCompressedLength = compressor.maxCompressedLength(dataInBytes.length);
        byte[] compressed = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(dataInBytes, 0, dataInBytes.length, compressed, 0, maxCompressedLength);
        log.info("maxCompressedLength : {}", maxCompressedLength);
        log.info("compressedLength : {}", compressedLength);
        return Arrays.copyOf(compressed, compressedLength);
    }

    public byte[] decompressBase64(byte[] compressedInBytes, int originalLength) {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4FastDecompressor decompressor = factory.fastDecompressor();
        byte[] restoredInBytes = new byte[originalLength];
        decompressor.decompress(compressedInBytes, 0, restoredInBytes, 0, originalLength);
        return restoredInBytes;
    }

}
