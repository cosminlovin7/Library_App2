package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppRegisterUserCmd;
import com.paj2ee.library.model.LibAppFile;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppFileRepository;
import com.paj2ee.library.repository.LibAppUserAuthorityRepository;
import com.paj2ee.library.repository.LibAppUserRepository;
import com.paj2ee.library.service.DiskStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@RestController
public class LibAppRegisterUserController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private LibAppUserAuthorityRepository libAppUserAuthorityRepository;
	@Autowired
	private LibAppFileRepository libAppFileRepository;
	@Autowired
	private DiskStorageService diskStorageServiceImpl;

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/register")
	public ResponseEntity<LinkedHashMap> registerUser(@RequestBody @Valid LibAppRegisterUserCmd cmd) {

		switch (cmd.fileType()) {
			case "application/pdf", "image/jpeg", "image.jpg", "image/png" -> {
				//all good
			}
			default -> {
				throw new RuntimeException("Invalid file type.");
			}
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Format it as a string
		String timestamp = now.format(formatter);

		String _fileName = extractFilename(cmd.fileName());
		String _extension = extractExtension(cmd.fileName());

		if (null == _fileName && null == _extension) {
			throw new RuntimeException("Invalid file name.");
		}
		String customFileName = _fileName + "_" + timestamp + "." + _extension;

		//@NOTE: These are the steps:
		// 1. Save the file metadata in the database with status: PENDING
		// 2. Save the file in the storage
		// 3. If successful, update the status: SUCCESS. Otherwise, update the status: ERROR

		//@STEP 1
		LibAppFile pendingLibAppFile = new LibAppFile();
		pendingLibAppFile.setFilename(customFileName);
		pendingLibAppFile.setType(cmd.fileType());
		pendingLibAppFile.setSize(cmd.fileSize());
		pendingLibAppFile.setStatus("PENDING");
		LibAppFile libAppFile = libAppFileRepository.save(pendingLibAppFile);

		//@STEP 2
		boolean status = diskStorageServiceImpl.saveFile(
			customFileName,
			cmd.fileData()
		);

		//@STEP 3
		if (true == status) {
			libAppFile.setStatus("SUCCESS");
		} else {
			libAppFile.setStatus("FAILURE");
		}

		LibAppFile updatedLibAppFile = libAppFileRepository.save(libAppFile);

		if (false == status) {
			throw new RuntimeException("Error occurred while trying to save the file");
		}

		LibAppUser pendingLibAppUser = new LibAppUser();

		pendingLibAppUser.setUsername(cmd.username());
		pendingLibAppUser.setPassword(cmd.password());
		pendingLibAppUser.setEnabled(false);
		pendingLibAppUser.setEmail(cmd.email());
		pendingLibAppUser.setPhoneNumber(cmd.phoneNumber());
		pendingLibAppUser.setIdentityCardFile(updatedLibAppFile);
		LibAppUser libAppUser = libAppUserRepository.save(pendingLibAppUser);

		LibAppUserAuthority pendingLibAppUserAuthority = new LibAppUserAuthority();
		pendingLibAppUserAuthority.setAuthority("ROLE_USER"); //@NOTE: Default role for all new accounts...
		pendingLibAppUserAuthority.setOwnerUser(libAppUser);

		LibAppUserAuthority libAppUserAuthority = libAppUserAuthorityRepository.save(pendingLibAppUserAuthority);

		return ResponseEntity.ok(new LinkedHashMap<String, Object>() {});

	}

	private String extractFilename(String fileName) {
		if (null == fileName) {
			return null;
		}

		String out = null;

		try {
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex == -1) {
				//nothing, the filename has no extension?
			} else {
				out = fileName.substring(0, dotIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	private String extractExtension(String fileName) {
		if (null == fileName) {
			return null;
		}

		String out = null;

		try {
			int dotIndex = fileName.lastIndexOf('.');
			if (dotIndex == -1) {
				//nothing, the filename has no extension?
			} else {
				out = fileName.substring(dotIndex + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}
}
