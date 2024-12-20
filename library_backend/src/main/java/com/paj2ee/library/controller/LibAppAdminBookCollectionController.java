package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppBookCollectionCmd;
import com.paj2ee.library.dto.BookCollectionDto;
import com.paj2ee.library.model.LibAppBookCollection;
import com.paj2ee.library.repository.LibAppBookCollectionRepository;
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
public class LibAppAdminBookCollectionController {

	@Autowired
	private LibAppBookCollectionRepository libAppBookCollectionRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/admin/book-collections/{id}")
	public ResponseEntity<BookCollectionDto> getCollection(@PathVariable("id") long id) {

		LibAppBookCollection libAppBookCollection = libAppBookCollectionRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book collection not found"));

		BookCollectionDto out = BookCollectionDto.fromEntity(libAppBookCollection);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/admin/book-collections")
	public ResponseEntity<List<BookCollectionDto>> getCollections() {

		List<LibAppBookCollection> bookCollectionLs = libAppBookCollectionRepository.findAll();

		List<BookCollectionDto> outLs = new ArrayList<>();
		for (LibAppBookCollection bookCollection : bookCollectionLs) {
			outLs.add(BookCollectionDto.fromEntity(bookCollection));
		}

		return ResponseEntity.ok(outLs);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/admin/book-collections/create-collection")
	public ResponseEntity<BookCollectionDto> createCollection(@Valid @RequestBody LibAppBookCollectionCmd cmd) {

		LibAppBookCollection pendingLibAppBookCollection = new LibAppBookCollection();

		pendingLibAppBookCollection.setName(cmd.name());

		LibAppBookCollection libAppBookCollection = libAppBookCollectionRepository.save(pendingLibAppBookCollection);

		BookCollectionDto out = BookCollectionDto.fromEntity(libAppBookCollection);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PutMapping("/admin/book-collections/update-collection/{id}")
	public ResponseEntity<BookCollectionDto> updateCollection(
		@PathVariable("id") long id,
		@Valid @RequestBody LibAppBookCollectionCmd cmd
	) {

		LibAppBookCollection libAppBookCollectionToUpdate = libAppBookCollectionRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book collection not found"));

		libAppBookCollectionToUpdate.setName(cmd.name());

		LibAppBookCollection libAppBookCollection = libAppBookCollectionRepository.save(libAppBookCollectionToUpdate);

		BookCollectionDto out = BookCollectionDto.fromEntity(libAppBookCollection);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@DeleteMapping("/admin/book-collections/delete-collection/{id}")
	public ResponseEntity<LinkedHashMap> deleteCollection(@PathVariable("id") long id) {

		LibAppBookCollection libAppBookCollectionToDelete = libAppBookCollectionRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book collection not found"));

		libAppBookCollectionRepository.delete(libAppBookCollectionToDelete);

		return ResponseEntity.ok(new LinkedHashMap());
	}

}
