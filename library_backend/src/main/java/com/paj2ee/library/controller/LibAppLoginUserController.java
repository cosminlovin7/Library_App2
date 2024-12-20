package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppLoginUserCmd;
import com.paj2ee.library.dto.FileInfoDto;
import com.paj2ee.library.dto.FileInfoMetaDto;
import com.paj2ee.library.dto.UserInfoDto;
import com.paj2ee.library.model.LibAppFile;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class LibAppLoginUserController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private DiskStorageService diskStorageServiceImpl;

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/login")
	public ResponseEntity<UserInfoDto> loginUser(@RequestBody @Valid LibAppLoginUserCmd libAppLoginUserCmd) {

		LibAppUser libAppUser = libAppUserRepository
			.findByUsername(libAppLoginUserCmd.username())
			.orElseThrow(() -> new RuntimeException("User not found"));

		Set<LibAppUserAuthority> libAppUserAuthorities = libAppUser.getAuthorities();

		List<String> authoritiesAsString = new ArrayList<>();
		for (LibAppUserAuthority libAppUserAuthority : libAppUserAuthorities) {
			authoritiesAsString.add(libAppUserAuthority.getAuthority());
		}

		LibAppFile libAppFile = libAppUser.getIdentityCardFile();

		String base64IdentityPhotoFile = null;

		FileInfoDto fileInfoDto = null;
		if (null != libAppFile) {
			base64IdentityPhotoFile = diskStorageServiceImpl.getFileAsBase64(libAppFile.getFilename());

			FileInfoMetaDto fileInfoMetaDto = new FileInfoMetaDto(
				libAppFile.getFilename(),
				libAppFile.getType(),
				libAppFile.getSize()
			);

			fileInfoDto = new FileInfoDto(
				libAppFile.getId(),
				fileInfoMetaDto,
				base64IdentityPhotoFile
			);
		}


		UserInfoDto userInfoDto = new UserInfoDto(
			libAppUser.getId(),
			libAppUser.getUsername(),
			libAppUser.getEmail(),
			libAppUser.isEnabled(),
			authoritiesAsString,
			fileInfoDto
		);

		return ResponseEntity.ok(userInfoDto);

	}

}
