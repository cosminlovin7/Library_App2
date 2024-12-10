package com.paj2ee.library.controller;

import com.paj2ee.library.dto.FileInfoDto;
import com.paj2ee.library.dto.FileInfoMetaDto;
import com.paj2ee.library.dto.UserInfoDto;
import com.paj2ee.library.model.LibAppFile;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserRepository;
import com.paj2ee.library.service.DiskStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController
public class LibAppDashboardHeaderController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private DiskStorageService diskStorageServiceImpl;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/dashboard-header")
	public ResponseEntity<UserInfoDto> getDashboardHeader(HttpServletRequest request) {

		UserInfoDto out = null;

		String authorizationHeader = request.getHeader("Authorization");

		String authorizationBasicToken = null;
		if (null != authorizationHeader) {
			authorizationBasicToken = authorizationHeader.substring("Basic ".length());
		}

		if (null != authorizationBasicToken) {
			try {
				byte[] decodedBytes = Base64.getDecoder().decode(authorizationBasicToken);
				String credentials = new String(decodedBytes);

				String[] parts = credentials.split(":", 2); // Split into at most 2 parts
				if (parts.length == 2) {
					String username = parts[0];
					String password = parts[1];

					LibAppUser libAppUser = libAppUserRepository
						.findByUsername(username)
						.orElseThrow(() -> new RuntimeException("Unidentified user"));

					Set<LibAppUserAuthority> libAppUserAuthorities = libAppUser.getAuthorities();
					List<String> authorities = new ArrayList<>();

					for (LibAppUserAuthority libAppUserAuthority : libAppUserAuthorities) {
						authorities.add(libAppUserAuthority.getAuthority());
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

					out = new UserInfoDto(
						libAppUser.getId(),
						libAppUser.getUsername(),
						libAppUser.isEnabled(),
						authorities,
						fileInfoDto
					);
				} else {
					throw new RuntimeException("Invalid credentials provided in authorization token");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ResponseEntity.ok(out);
	}

}
