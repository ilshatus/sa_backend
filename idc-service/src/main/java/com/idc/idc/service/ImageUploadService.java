package com.idc.idc.service;

public interface ImageUploadService {
    String getExtension(byte[] data);
    String uploadPublicImage(byte[] data, String filePath);
}
