package com.paj2ee.library.controller;

import com.paj2ee.library.dto.FileInfoDto;
import com.paj2ee.library.dto.FileInfoMetaDto;
import com.paj2ee.library.dto.UserInfoDto;
import com.paj2ee.library.model.LibAppFile;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserRepository;
import com.paj2ee.library.service.DiskStorageService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class LibAppUserController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private DiskStorageService diskStorageServiceImpl;

	@GetMapping("/users")
	public ResponseEntity<List<UserInfoDto>> getAllUsers(
		@RequestParam(name = "enabled", required = false, defaultValue = "false") boolean enabled
	) {
		Specification<LibAppUser> allSpec =
			rootSpec();

		if (true == enabled) {
			allSpec = allSpec.and(isUserEnabled());
		}

		List<LibAppUser> users = libAppUserRepository.findAll(allSpec);

		List<UserInfoDto> usersDto = new ArrayList<>();
		for (LibAppUser user : users) {

			Set<LibAppUserAuthority> libAppUserAuthorities = user.getAuthorities();
			List<String> authorities = new ArrayList<>();

			for (LibAppUserAuthority libAppUserAuthority : libAppUserAuthorities) {
				authorities.add(libAppUserAuthority.getAuthority());
			}

			LibAppFile libAppFile = user.getIdentityCardFile();

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
					fileInfoMetaDto,
					base64IdentityPhotoFile
				);
			}

			UserInfoDto userInfoDto = new UserInfoDto(
				user.getUsername(),
				user.isEnabled(),
				authorities,
				fileInfoDto
			);

			usersDto.add(userInfoDto);
		}

		return ResponseEntity.ok(usersDto);
	}

	private static Specification<LibAppUser> rootSpec() {
		return (root, query, builder) -> {
			return builder.conjunction();
		};
	}

	private static Specification<LibAppUser> isUserEnabled() {
		return (root, query, builder) -> {
			return builder.equal(root.get("enabled"), true);
		};
	}

}
