package com.amkart.estore.services;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface Fileservice {
String uploadFile(MultipartFile file, String path);
InputStream getFile(String path, String filename);
}
