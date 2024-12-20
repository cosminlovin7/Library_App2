package com.paj2ee.library.controller;

import com.paj2ee.library.dto.BookDto;
import com.paj2ee.library.model.LibAppBook;
import com.paj2ee.library.repository.LibAppBookRepository;
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
public class LibAppBookController {

	@Autowired
	private LibAppBookRepository libAppBookRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/books/{id}")
	public ResponseEntity<BookDto> getBook(@PathVariable Long id) {

		LibAppBook libAppBook = libAppBookRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book not found"));

		BookDto out = BookDto.fromEntity(libAppBook);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/books")
	public ResponseEntity<List<BookDto>> getBooks() {

		List<LibAppBook> libAppBookLs = libAppBookRepository.findAll();

		List<BookDto> outLs = new ArrayList<BookDto>();
		for (LibAppBook libAppBook : libAppBookLs) {
			outLs.add(BookDto.fromEntity(libAppBook));
		}

		return ResponseEntity.ok(outLs);
	}
}
