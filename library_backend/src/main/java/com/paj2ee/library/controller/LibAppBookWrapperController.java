package com.paj2ee.library.controller;

import com.paj2ee.library.dto.BookWrapperDto;
import com.paj2ee.library.model.LibAppBookWrapper;
import com.paj2ee.library.repository.LibAppBookRepository;
import com.paj2ee.library.repository.LibAppBookWrapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LibAppBookWrapperController {

	@Autowired
	private LibAppBookRepository libAppBookRepository;

	@Autowired
	private LibAppBookWrapperRepository libAppBookWrapperRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/book-wrappers")
	public ResponseEntity<List<BookWrapperDto>> getBookWrappers() {

		List<LibAppBookWrapper> libAppBookWrapperLs = libAppBookWrapperRepository.findAll();

		List<BookWrapperDto> outLs = new ArrayList<>();
		for(LibAppBookWrapper libAppBookWrapper : libAppBookWrapperLs) {
			outLs.add(BookWrapperDto.fromEntity(libAppBookWrapper));
		}

		return ResponseEntity.ok(outLs);

	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/book-wrappers/{id}")
	public ResponseEntity<BookWrapperDto> getBookWrapper(@PathVariable("id") Long id) {

		LibAppBookWrapper libAppBookWrapper = libAppBookWrapperRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book wrapper not found"));

		BookWrapperDto out = BookWrapperDto.fromEntity(libAppBookWrapper);

		return ResponseEntity.ok(out);

	}

}
