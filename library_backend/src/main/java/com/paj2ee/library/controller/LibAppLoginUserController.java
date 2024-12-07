package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppLoginUserCmd;
import com.paj2ee.library.dto.UserInfoDto;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

		UserInfoDto userInfoDto = new UserInfoDto(
			libAppUser.getUsername(),
			libAppUser.isEnabled(),
			authoritiesAsString
		);

		return ResponseEntity.ok(userInfoDto);

	}

}
