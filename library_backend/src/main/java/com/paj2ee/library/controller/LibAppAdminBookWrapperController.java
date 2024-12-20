package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppCreateBookWrapperCmd;
import com.paj2ee.library.cmd.LibAppUpdateBookWrapperCmd;
import com.paj2ee.library.dto.BookWrapperDto;
import com.paj2ee.library.model.LibAppBook;
import com.paj2ee.library.model.LibAppBookWrapper;
import com.paj2ee.library.repository.LibAppBookRepository;
import com.paj2ee.library.repository.LibAppBookWrapperRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class LibAppAdminBookWrapperController {

	@Autowired
	private LibAppBookRepository libAppBookRepository;

	@Autowired
	private LibAppBookWrapperRepository libAppBookWrapperRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/admin/book-wrappers")
	public ResponseEntity<List<BookWrapperDto>> getBookWrappers() {

		List<LibAppBookWrapper> libAppBookWrapperLs = libAppBookWrapperRepository.findAll();

		List<BookWrapperDto> outLs = new ArrayList<>();
		for(LibAppBookWrapper libAppBookWrapper : libAppBookWrapperLs) {
			outLs.add(BookWrapperDto.fromEntity(libAppBookWrapper));
		}

		return ResponseEntity.ok(outLs);

	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/admin/book-wrappers/{id}")
	public ResponseEntity<BookWrapperDto> getBookWrapper(@PathVariable("id") Long id) {

		LibAppBookWrapper libAppBookWrapper = libAppBookWrapperRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book wrapper not found"));

		BookWrapperDto out = BookWrapperDto.fromEntity(libAppBookWrapper);

		return ResponseEntity.ok(out);

	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/admin/book-wrappers/create-wrapper")
	public ResponseEntity<BookWrapperDto> createBookWrapper(@Valid @RequestBody LibAppCreateBookWrapperCmd cmd) {

		LibAppBook libAppBook = libAppBookRepository
			.findById(cmd.ownerLibAppBookId())
			.orElseThrow(() -> new RuntimeException("Book not found"));

		LibAppBookWrapper pendingLibAppBookWrapper = new LibAppBookWrapper();
		pendingLibAppBookWrapper.setOwnerLibAppBook(libAppBook);
		pendingLibAppBookWrapper.setQuantity(cmd.quantity());
		pendingLibAppBookWrapper.setAvailableQuantity(cmd.availableQuantity());

		LibAppBookWrapper libAppBookWrapper = libAppBookWrapperRepository.save(pendingLibAppBookWrapper);

		BookWrapperDto out = BookWrapperDto.fromEntity(libAppBookWrapper);

		return ResponseEntity.ok(out);

	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PutMapping("/admin/book-wrappers/{id}/update-wrapper")
	public ResponseEntity<BookWrapperDto> updateBookWrapper(
		@PathVariable("id") Long id,
		@Valid @RequestBody LibAppUpdateBookWrapperCmd cmd
	) {

		LibAppBookWrapper libAppBookWrapperToUpdate = libAppBookWrapperRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book wrapper not found"));

		libAppBookWrapperToUpdate.setQuantity(cmd.quantity());
		libAppBookWrapperToUpdate.setAvailableQuantity(cmd.availableQuantity());

		LibAppBookWrapper libAppBookWrapper = libAppBookWrapperRepository.save(libAppBookWrapperToUpdate);

		BookWrapperDto out = BookWrapperDto.fromEntity(libAppBookWrapper);

		return ResponseEntity.ok(out);

	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@DeleteMapping("/admin/book-wrappers/{id}/delete-wrapper")
	public ResponseEntity<LinkedHashMap> deleteBookWrapper(@PathVariable("id") Long id) {

		LibAppBookWrapper libAppBookWrapperToDelete = libAppBookWrapperRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book wrapper not found"));

		LibAppBook libAppBook = libAppBookWrapperToDelete.getOwnerLibAppBook();

		libAppBook.setLibAppBookWrapper(null);

		return ResponseEntity.ok(new LinkedHashMap());

	}

}
