package com.amkart.estore.services.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.nio.file.Files;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amkart.estore.services.Fileservice;

@Service
public class FileServiceImp implements Fileservice {

	@Override
	public String uploadFile(MultipartFile file, String filepath) {
		String originalname = file.getOriginalFilename();
		String extension = originalname.substring(originalname.lastIndexOf('.'));
		String newfilename = UUID.randomUUID().toString() + extension;
		String fullpath = filepath + File.separator + newfilename;
		File folder = new File(filepath);
		if (!folder.exists())
			folder.mkdirs();
		try {
			Files.copy(file.getInputStream(), Paths.get(fullpath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newfilename;
	}

	@Override
	public InputStream getFile(String path, String filename) {
		// TODO Auto-generated method stub
		String fullpath = path + File.separator + filename;
		InputStream inputstream;
		try {
			inputstream = new FileInputStream(fullpath);
			return inputstream;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
