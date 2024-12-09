package com.paj2ee.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class DiskStorageServiceImpl implements DiskStorageService {

	private static final Logger logger = LoggerFactory.getLogger(DiskStorageServiceImpl.class);

	@Value("${libapp.storage.path}")
	private String storagePath;

	@Override
	public boolean saveFile(String fileName, String base64FileContent) {
		if (null == fileName) {
			return false;
		}
		if (null == base64FileContent) {
			return false;
		}

		boolean fileSavedSuccessfully = false;

		try {
			String base64Encoded = base64FileContent.split(",")[1];
			byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);

			String customFileStoragePath = storagePath + "/" + fileName;

			FileOutputStream fileOutputStream = new FileOutputStream(customFileStoragePath);

			fileOutputStream.write(decodedBytes);

			fileSavedSuccessfully = true;
			logger.info("File successfully saved at: {}.", customFileStoragePath);
		} catch (Exception e) {
			e.printStackTrace();

			logger.error("Could not save file: {}.", fileName);
		}

		return fileSavedSuccessfully;
	}

	@Override
	public String getFileAsBase64(String fileName) {
		if (null == fileName) {
			return null;
		}

		String base64FileContent = null;

		String customFileStoragePath = storagePath + "/" + fileName;

		try {
			Path filePath = Paths.get(customFileStoragePath);

			byte[] fileBytes = Files.readAllBytes(filePath);

			base64FileContent = Base64.getEncoder().encodeToString(fileBytes);

			logger.info("File successfully retrieved from: {}.", customFileStoragePath);
		} catch(Exception e) {
			e.printStackTrace();

			logger.error("Could not retrieve file: {}.", fileName);
		}

		return base64FileContent;
	}
}
