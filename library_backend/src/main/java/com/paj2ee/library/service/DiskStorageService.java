package com.paj2ee.library.service;

public interface DiskStorageService {

	boolean saveFile(String fileName, String base64FileContent);
	String getFileAsBase64(String fileName);

}
