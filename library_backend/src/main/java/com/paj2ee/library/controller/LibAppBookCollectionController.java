package com.paj2ee.library.controller;

import com.paj2ee.library.dto.BookCollectionDto;
import com.paj2ee.library.model.LibAppBookCollection;
import com.paj2ee.library.repository.LibAppBookCollectionRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LibAppBookCollectionController {

	@Autowired
	private LibAppBookCollectionRepository libAppBookCollectionRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/book-collections/{id}")
	public ResponseEntity<BookCollectionDto> getCollection(@PathVariable("id") long id) {

		LibAppBookCollection libAppBookCollection = libAppBookCollectionRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book collection not found"));

		BookCollectionDto out = BookCollectionDto.fromEntity(libAppBookCollection);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/book-collections")
	public ResponseEntity<List<BookCollectionDto>> getCollections(HttpServletResponse response) {

		List<LibAppBookCollection> bookCollectionLs = libAppBookCollectionRepository.findAll();

		bookCollectionLs.sort((o1, o2) -> {
			if (o1.getId() < o2.getId()) {
				return -1;
			} else if (o1.getId() > o2.getId()) {
				return 1;
			}

			return 0;
		});

		List<BookCollectionDto> outLs = new ArrayList<>();
		for (LibAppBookCollection bookCollection : bookCollectionLs) {
			outLs.add(BookCollectionDto.fromEntity(bookCollection));
		}

//		response.setHeader(HttpHeaders.CACHE_CONTROL, "public, max-age=3600");

		return ResponseEntity.ok(outLs);
	}

}
