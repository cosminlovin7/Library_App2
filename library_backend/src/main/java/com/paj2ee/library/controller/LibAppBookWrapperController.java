package com.paj2ee.library.controller;

import com.paj2ee.library.dto.BookWrapperDto;
import com.paj2ee.library.dto.PageOfBookWrapperDto;
import com.paj2ee.library.model.LibAppBook;
import com.paj2ee.library.model.LibAppBookCollection;
import com.paj2ee.library.model.LibAppBookWrapper;
import com.paj2ee.library.repository.LibAppBookRepository;
import com.paj2ee.library.repository.LibAppBookWrapperRepository;
import jakarta.persistence.criteria.Join;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<PageOfBookWrapperDto> getBookWrappers(
		@RequestParam(name = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
		@RequestParam(name = "title", required = false) String title,
		@RequestParam(name = "author", required = false) String author,
		@RequestParam(name = "editure", required = false) String editure,
		@RequestParam(name = "collection", required = false) Long collectionId,
		@RequestParam(name = "yearOfPublication", required = false) Integer yearOfPublication
	) {

		Specification<LibAppBookWrapper> allSpec = rootSpec();

		if (null != title) {
			allSpec = allSpec.and(compareWithTitleSpec(title));
		}
		if (null != author) {
			allSpec = allSpec.and(compareWithAuthorSpec(author));
		}
		if (null != editure) {
			allSpec = allSpec.and(compareWithEditureSpec(editure));
		}
		if (null != collectionId) {
			allSpec = allSpec.and(compareWithCollectionSpec(collectionId));
		}
		if (null != yearOfPublication) {
			allSpec = allSpec.and(compareWithYearOfPublicationSpec(yearOfPublication));
		}

		Pageable pageOptions = PageRequest.of(page, pageSize, Sort.by("id").ascending());
		Page<LibAppBookWrapper> pageOfLibAppBookWrapper = libAppBookWrapperRepository.findAll(allSpec, pageOptions);

		List<LibAppBookWrapper> libAppBookWrapperLs = pageOfLibAppBookWrapper.getContent();
		long total = pageOfLibAppBookWrapper.getTotalElements();
		List<BookWrapperDto> outLs = new ArrayList<>();
		for(LibAppBookWrapper libAppBookWrapper : libAppBookWrapperLs) {
			outLs.add(BookWrapperDto.fromEntity(libAppBookWrapper));
		}

		PageOfBookWrapperDto out = new PageOfBookWrapperDto(
			total,
			outLs
		);

		return ResponseEntity.ok(out);

	}

	private static Specification<LibAppBookWrapper> rootSpec() {
		return (root, query, builder) -> {
			return builder.conjunction();
		};
	}

	private static Specification<LibAppBookWrapper> compareWithTitleSpec(String title) {
		return (root, query, builder) -> {
			Join<LibAppBookWrapper, LibAppBook> child = root.join("ownerLibAppBook");

			return builder.like(child.get("title"), "%" + title + "%");
		};
	}

	private static Specification<LibAppBookWrapper> compareWithAuthorSpec(String author) {
		return (root, query, builder) -> {
			Join<LibAppBookWrapper, LibAppBook> child = root.join("ownerLibAppBook");

			return builder.like(child.get("author"), "%" + author + "%");
		};
	}

	private static Specification<LibAppBookWrapper> compareWithEditureSpec(String editure) {
		return (root, query, builder) -> {
			Join<LibAppBookWrapper, LibAppBook> child = root.join("ownerLibAppBook");

			return builder.like(child.get("editure"), "%" + editure + "%");
		};
	}

	private static Specification<LibAppBookWrapper> compareWithCollectionSpec(Long collectionId) {
		return (root, query, builder) -> {
			Join<LibAppBookWrapper, LibAppBook> child = root.join("ownerLibAppBook");
			Join<LibAppBook, LibAppBookCollection> bookCollectionChild = child.join("collection");

			return builder.equal(bookCollectionChild.get("id"), collectionId);
		};
	}

	private static Specification<LibAppBookWrapper> compareWithYearOfPublicationSpec(Integer yearOfPublication) {
		return (root, query, builder) -> {
			Join<LibAppBookWrapper, LibAppBook> child = root.join("ownerLibAppBook");

			return builder.equal(child.get("yearOfPublication"), yearOfPublication);
		};
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
