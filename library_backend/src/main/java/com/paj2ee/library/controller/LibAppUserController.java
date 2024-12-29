package com.paj2ee.library.controller;

import com.paj2ee.library.dto.FileInfoDto;
import com.paj2ee.library.dto.FileInfoMetaDto;
import com.paj2ee.library.dto.PageOfUserInfoDto;
import com.paj2ee.library.dto.UserInfoDto;
import com.paj2ee.library.model.LibAppFile;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserRepository;
import com.paj2ee.library.service.DiskStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/users")
	public ResponseEntity<PageOfUserInfoDto> getAllUsers(
		@RequestParam(name = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
		@RequestParam(name = "userStatus", required = false, defaultValue = "any") String userStatus
	) {
		Specification<LibAppUser> allSpec =
			rootSpec();

		switch (userStatus) {
			case "enabled" -> {
				allSpec = allSpec.and(isUserEnabled());
			}
			case "disabled" -> {
				allSpec = allSpec.and(Specification.not(isUserEnabled()));
			}
			case "any" -> {
				//noop
			}
			default -> {
				throw new RuntimeException("Invalid userStatus param");
			}
		}

		Pageable pageOptions = PageRequest.of(page, pageSize, Sort.by("id").ascending());

		Page<LibAppUser> usersPage = libAppUserRepository.findAll(allSpec, pageOptions);

		List<LibAppUser> usersLs = usersPage.getContent();
		long total = usersPage.getTotalElements();
		List<UserInfoDto> usersDto = new ArrayList<>();
		for (LibAppUser user : usersLs) {

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
					libAppFile.getId(),
					fileInfoMetaDto,
					base64IdentityPhotoFile
				);
			}

			UserInfoDto userInfoDto = new UserInfoDto(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.isEnabled(),
				authorities,
				fileInfoDto
			);

			usersDto.add(userInfoDto);
		}

		PageOfUserInfoDto pageOfUserInfoDto = new PageOfUserInfoDto(
			total,
			usersDto
		);

		return ResponseEntity.ok(pageOfUserInfoDto);
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
