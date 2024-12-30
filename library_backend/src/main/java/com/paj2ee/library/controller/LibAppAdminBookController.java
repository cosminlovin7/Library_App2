package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppCreateBookCmd;
import com.paj2ee.library.cmd.LibAppUpdateBookCmd;
import com.paj2ee.library.dto.BookDto;
import com.paj2ee.library.dto.PageOfBookDto;
import com.paj2ee.library.model.LibAppBook;
import com.paj2ee.library.model.LibAppBookCollection;
import com.paj2ee.library.repository.LibAppBookCollectionRepository;
import com.paj2ee.library.repository.LibAppBookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class LibAppAdminBookController {

	@Autowired
	private LibAppBookRepository libAppBookRepository;
	@Autowired
	private LibAppBookCollectionRepository libAppBookCollectionRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/admin/books")
	public ResponseEntity<PageOfBookDto> getBooks(
		@RequestParam(name = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
		@RequestParam(name = "title", required = false) String title,
		@RequestParam(name = "author", required = false) String author,
		@RequestParam(name = "editure", required = false) String editure,
		@RequestParam(name = "collection", required = false) String collection,
		@RequestParam(name = "yearOfPublication", required = false) Integer yearOfPublication
	) {

		Specification<LibAppBook> allSpec = rootSpec();

		if (null != title) {
			allSpec = allSpec.and(compareWithTitleSpec(title));
		}
		if (null != author) {
			allSpec = allSpec.and(compareWithAuthorSpec(author));
		}
		if (null != editure) {
			allSpec = allSpec.and(compareWithEditureSpec(editure));
		}
		if (null != collection) {
			allSpec = allSpec.and(compareWithCollectionSpec(collection));
		}
		if (null != yearOfPublication) {
			allSpec = allSpec.and(compareWithYearOfPublicationSpec(yearOfPublication));
		}

		Pageable pageOptions = PageRequest.of(page, pageSize, Sort.by("id").ascending());
		Page<LibAppBook> pageOfLibAppBook = libAppBookRepository.findAll(allSpec, pageOptions);

		List<LibAppBook> libAppBookLs = pageOfLibAppBook.getContent();
		long count = pageOfLibAppBook.getTotalElements();
		List<BookDto> outLs = new ArrayList<>();
		for (LibAppBook libAppBook : libAppBookLs) {
			outLs.add(BookDto.fromEntity(libAppBook));
		}

		PageOfBookDto out = new PageOfBookDto(
			count,
			outLs
		);

		return ResponseEntity.ok(out);
	}

	private static Specification<LibAppBook> rootSpec() {
		return (root, query, builder) -> {
			return builder.conjunction();
		};
	}

	private static Specification<LibAppBook> compareWithTitleSpec(String title) {
		return (root, query, builder) -> {
			return builder.like(root.get("title"), "%" + title + "%");
		};
	}

	private static Specification<LibAppBook> compareWithAuthorSpec(String author) {
		return (root, query, builder) -> {
			return builder.like(root.get("author"), "%" + author + "%");
		};
	}

	private static Specification<LibAppBook> compareWithEditureSpec(String editure) {
		return (root, query, builder) -> {
			return builder.like(root.get("editure"), "%" + editure + "%");
		};
	}

	private static Specification<LibAppBook> compareWithCollectionSpec(String collection) {
		return (root, query, builder) -> {
			return builder.like(root.get("collection"), "%" + collection + "%");
		};
	}

	private static Specification<LibAppBook> compareWithYearOfPublicationSpec(Integer yearOfPublication) {
		return (root, query, builder) -> {
			return builder.equal(root.get("yearOfPublication"), yearOfPublication);
		};
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/admin/books/{id}")
	public ResponseEntity<BookDto> getBook(@PathVariable Long id) {

		LibAppBook libAppBook = libAppBookRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book not found"));

		BookDto out = BookDto.fromEntity(libAppBook);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/admin/books/create-book")
	public ResponseEntity<BookDto> createBook(@Valid @RequestBody LibAppCreateBookCmd cmd) {

		LibAppBook pendingLibAppBook = new LibAppBook();

		pendingLibAppBook.setTitle(cmd.title());
		pendingLibAppBook.setAuthor(cmd.author());
		pendingLibAppBook.setEditure(cmd.editure());
		pendingLibAppBook.setEdition(cmd.edition());
		pendingLibAppBook.setIsbn(cmd.isbn());
		pendingLibAppBook.setTranslator(cmd.translator());
		pendingLibAppBook.setPageNr(cmd.pageNr());
		pendingLibAppBook.setBookFormat(cmd.bookFormat());
		pendingLibAppBook.setCoverType(LibAppBook.BookCoverType.valueOf(cmd.coverType()));
		pendingLibAppBook.setYearOfPublication(cmd.yearOfPublication());

		LibAppBook libAppBook = libAppBookRepository.save(pendingLibAppBook);

		BookDto out = BookDto.fromEntity(libAppBook);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PutMapping("/admin/books/update-book/{id}")
	public ResponseEntity<BookDto> updateBook(
		@PathVariable("id") long id,
		@Valid @RequestBody LibAppUpdateBookCmd cmd
	) {

		LibAppBook libAppBookToUpdate = libAppBookRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book not found"));

		libAppBookToUpdate.setTitle(cmd.title());
		libAppBookToUpdate.setAuthor(cmd.author());
		libAppBookToUpdate.setEditure(cmd.editure());
		libAppBookToUpdate.setEdition(cmd.edition());
		libAppBookToUpdate.setIsbn(cmd.isbn());
		libAppBookToUpdate.setTranslator(cmd.translator());
		libAppBookToUpdate.setPageNr(cmd.pageNr());
		libAppBookToUpdate.setBookFormat(cmd.bookFormat());
		libAppBookToUpdate.setCoverType(LibAppBook.BookCoverType.valueOf(cmd.coverType()));
		libAppBookToUpdate.setYearOfPublication(cmd.yearOfPublication());

		LibAppBook libAppBook = libAppBookRepository.save(libAppBookToUpdate);

		BookDto out = BookDto.fromEntity(libAppBook);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@DeleteMapping("/admin/books/delete-book/{id}")
	public ResponseEntity<LinkedHashMap> deleteBook(@PathVariable("id") long id) {

		LibAppBook libAppBookToDelete = libAppBookRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book not found"));

		libAppBookRepository.delete(libAppBookToDelete);

		return ResponseEntity.ok(new LinkedHashMap());
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PutMapping("/admin/books/{bookId}/book-collections/{bookCollectionId}/add")
	public ResponseEntity<BookDto> addBookToBookCategory(
		@PathVariable("bookId") long bookId,
		@PathVariable("bookCollectionId") long bookCollectionId
	) {

		LibAppBook libAppBookToAdd = libAppBookRepository
			.findById(bookId)
			.orElseThrow(() -> new RuntimeException("Book not found"));

		LibAppBookCollection libAppBookCollection = libAppBookCollectionRepository
			.findById(bookCollectionId)
			.orElseThrow(() -> new RuntimeException("Book collection not found"));

		if (null != libAppBookToAdd.getCollection()) {
			throw new RuntimeException("Book is already part of a collection");
		}

		libAppBookToAdd.setCollection(libAppBookCollection);

		LibAppBook libAppBook = libAppBookRepository.save(libAppBookToAdd);

		BookDto out = BookDto.fromEntity(libAppBook);
		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PutMapping("/admin/books/{bookId}/book-collections/{bookCollectionId}/remove")
	public ResponseEntity<BookDto> removeBookFromBookCategory(
		@PathVariable("bookId") long bookId,
		@PathVariable("bookCollectionId") long bookCollectionId
	) {

		LibAppBook libAppBookToRemove = libAppBookRepository
			.findById(bookId)
			.orElseThrow(() -> new RuntimeException("Book not found"));

		LibAppBookCollection libAppBookCollection = libAppBookCollectionRepository
			.findById(bookCollectionId)
			.orElseThrow(() -> new RuntimeException("Book collection not found"));

		LibAppBookCollection libAppBookToRemoveCollection = libAppBookToRemove.getCollection();

		if (null == libAppBookToRemoveCollection) {
			throw new RuntimeException("Book has no collection");
		}

		if (libAppBookToRemoveCollection.getId() != libAppBookCollection.getId()) {
			throw new RuntimeException("Bad request");
		}

		libAppBookToRemove.setCollection(null);

		LibAppBook libAppBook = libAppBookRepository.save(libAppBookToRemove);

		BookDto out = BookDto.fromEntity(libAppBook);
		return ResponseEntity.ok(out);
	}
}
