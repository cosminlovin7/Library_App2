package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppRegisterUserCmd;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserAuthorityRepository;
import com.paj2ee.library.repository.LibAppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibAppRegisterUserController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private LibAppUserAuthorityRepository libAppUserAuthorityRepository;

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody @Valid LibAppRegisterUserCmd cmd) {

		LibAppUser pendingLibAppUser = new LibAppUser();

		pendingLibAppUser.setUsername(cmd.username());
		pendingLibAppUser.setPassword(cmd.password());
		pendingLibAppUser.setEnabled(false);
		pendingLibAppUser.setPhoneNumber(cmd.phoneNumber());

		LibAppUser libAppUser = libAppUserRepository.save(pendingLibAppUser);

		LibAppUserAuthority pendingLibAppUserAuthority = new LibAppUserAuthority();
		pendingLibAppUserAuthority.setAuthority("ROLE_USER"); //@NOTE: Default role for all new accounts...
		pendingLibAppUserAuthority.setOwnerUser(libAppUser);

		LibAppUserAuthority libAppUserAuthority = libAppUserAuthorityRepository.save(pendingLibAppUserAuthority);

		return ResponseEntity.ok("User created successfully!");

	}

}
