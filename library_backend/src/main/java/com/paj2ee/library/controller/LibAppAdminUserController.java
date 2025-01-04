package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppUpdateUserEmailCmd;
import com.paj2ee.library.cmd.LibAppUpdateUserPhoneNumberCmd;
import com.paj2ee.library.cmd.LibAppUpdateUserUsernameCmd;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@RestController
public class LibAppAdminUserController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;

	@Autowired
	private DiskStorageService diskStorageServiceImpl;

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/admin/users/{id}/enable")
	public ResponseEntity<LinkedHashMap> enableUser(@PathVariable("id") long id) {

		LibAppUser libAppUserToEnable = libAppUserRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));

		if (libAppUserToEnable.isEnabled()) {
			throw new RuntimeException("User already enabled");
		}

		libAppUserToEnable.setEnabled(true);

		libAppUserRepository.save(libAppUserToEnable);

		return ResponseEntity.ok(new LinkedHashMap());
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/admin/users/{id}/disable")
	public ResponseEntity<LinkedHashMap> disableUser(@PathVariable("id") long id) {

		LibAppUser libAppUserToDisable = libAppUserRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));

		if (! libAppUserToDisable.isEnabled()) {
			throw new RuntimeException("User already disabled");
		}

		libAppUserToDisable.setEnabled(false);

		libAppUserRepository.save(libAppUserToDisable);

		return ResponseEntity.ok(new LinkedHashMap());
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@DeleteMapping("/admin/users/{id}/delete-user")
	public ResponseEntity<LinkedHashMap> deleteUser(@PathVariable("id") long id) {

		LibAppUser libAppUserToDelete = libAppUserRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));

		libAppUserRepository.delete(libAppUserToDelete);

		return ResponseEntity.ok(new LinkedHashMap());
	}

}
