package com.paj2ee.library.controller;

import com.paj2ee.library.cmd.LibAppCreateBookLoanCmd;
import com.paj2ee.library.cmd.LibAppUpdateBookLoanCmd;
import com.paj2ee.library.dto.BookLoanDto;
import com.paj2ee.library.model.LibAppBookLoan;
import com.paj2ee.library.model.LibAppBookWrapper;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.repository.LibAppBookLoanRepository;
import com.paj2ee.library.repository.LibAppBookWrapperRepository;
import com.paj2ee.library.repository.LibAppUserRepository;
import com.paj2ee.library.service.BookLoanService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class LibAppBookLoanController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private LibAppBookWrapperRepository libAppBookWrapperRepository;
	@Autowired
	private LibAppBookLoanRepository libAppBookLoanRepository;

	@Autowired
	private BookLoanService bookLoanService;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/book-loans")
	public ResponseEntity<List<BookLoanDto>> getBookLoans() {

		List<LibAppBookLoan> libAppBookLoanLs = libAppBookLoanRepository.findAll();

		List<BookLoanDto> outLs = new ArrayList<>();
		for (LibAppBookLoan libAppBookLoan : libAppBookLoanLs) {
			outLs.add(BookLoanDto.fromEntity(libAppBookLoan));
		}

		return ResponseEntity.ok(outLs);
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/book-loans/{id}")
	public ResponseEntity<BookLoanDto> getBookLoans(@PathVariable("id") Long id) {

		LibAppBookLoan libAppBookLoan = libAppBookLoanRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book loan not found"));

		BookLoanDto out = BookLoanDto.fromEntity(libAppBookLoan);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/book-loans/create-book-loan")
	public ResponseEntity<BookLoanDto> createBookLoan(@Valid @RequestBody LibAppCreateBookLoanCmd cmd) {

		LibAppUser libAppUser = libAppUserRepository
			.findById(cmd.ownerUserId())
			.orElseThrow(() -> new RuntimeException("User not found"));

		LibAppBookWrapper libAppBookWrapper = libAppBookWrapperRepository
			.findById(cmd.loanedBookWrapperId())
			.orElseThrow(() -> new RuntimeException("Book not found"));

		if (libAppBookWrapper.getAvailableQuantity() == 0) {
			throw new RuntimeException("Book not available");
		}

		if (! bookLoanService.isUserEligibleForBookLoan(cmd.ownerUserId())) {
			throw new RuntimeException("User not eligible for book loans");
		}

		libAppBookWrapper.decreaseAvailableQuantity();

		LibAppBookLoan pendingLibAppBookLoan = new LibAppBookLoan();

//		pendingLibAppBookLoan.setLoanedOn(LocalDateTime.now());
//		pendingLibAppBookLoan.setLoanExpireOn(LocalDateTime.now().plusDays(30));
		pendingLibAppBookLoan.setLoanedOn(cmd.loanedOn());
		pendingLibAppBookLoan.setLoanExpireOn(cmd.loanExpireOn());
		pendingLibAppBookLoan.setReturnedOn(null);
		pendingLibAppBookLoan.setLoanStatus(LibAppBookLoan.BookLoanStatus.LOANED);
		pendingLibAppBookLoan.setLoanFineAmount(BigDecimal.valueOf(0.0));
		pendingLibAppBookLoan.setLoanedBookWrapper(libAppBookWrapper);
		pendingLibAppBookLoan.setOwnerUser(libAppUser);

		LibAppBookLoan libAppBookLoan = libAppBookLoanRepository.save(pendingLibAppBookLoan);

		BookLoanDto out = BookLoanDto.fromEntity(libAppBookLoan);

		return ResponseEntity.ok(out);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostMapping("/book-loans/{id}/end-book-loan")
	public ResponseEntity<BookLoanDto> endBookLoan(@PathVariable("id") long id) {

		LibAppBookLoan libAppBookLoanToEnd = libAppBookLoanRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book loan not found"));

		if (libAppBookLoanToEnd.getLoanStatus() == LibAppBookLoan.BookLoanStatus.RETURNED) {
			throw new RuntimeException("Book already returned from loan");
		}

		LibAppBookWrapper libAppBookWrapper = libAppBookLoanToEnd.getLoanedBookWrapper();
		libAppBookWrapper.increaseAvailableQuantity();
		libAppBookLoanToEnd.setReturnedOn(LocalDateTime.now());
		libAppBookLoanToEnd.setLoanStatus(LibAppBookLoan.BookLoanStatus.RETURNED);

		BookLoanDto out = BookLoanDto.fromEntity(libAppBookLoanToEnd);

		return ResponseEntity.ok(out);
	}

//	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
//	@PutMapping("/book-loans/{id}/update-book-loan")
//	public ResponseEntity<BookLoanDto> updateBookLoan(
//		@PathVariable("id") long id,
//		@Valid @RequestBody LibAppUpdateBookLoanCmd cmd
//	) {
//
//		LibAppUser libAppUser = libAppUserRepository
//			.findById(cmd.ownerUserId())
//			.orElseThrow(() -> new RuntimeException("User not found"));
//
//		LibAppBookWrapper libAppBookWrapper = libAppBookWrapperRepository
//			.findById(cmd.loanedBookWrapperId())
//			.orElseThrow(() -> new RuntimeException("Book not found"));
//
//		LibAppBookLoan libAppBookLoanToUpdate = libAppBookLoanRepository
//			.findById(id)
//			.orElseThrow(() -> new RuntimeException("Book loan not found"));
//
//		if (libAppBookLoanToUpdate.getLoanStatus() == LibAppBookLoan.BookLoanStatus.RETURNED) {
//			throw new RuntimeException("Book already returned from loan. Cannot update book loan");
//		}
//
//		libAppBookLoanToUpdate.setLoanedOn(cmd.loanedOn());
//		libAppBookLoanToUpdate.setLoanExpireOn(cmd.loanExpireOn());
//		libAppBookLoanToUpdate.setLoanedBookWrapper(libAppBookWrapper);
//		libAppBookLoanToUpdate.setOwnerUser(libAppUser);
//
//		LibAppBookLoan libAppBookLoan = libAppBookLoanRepository.save(libAppBookLoanToUpdate);
//
//		BookLoanDto out = BookLoanDto.fromEntity(libAppBookLoan);
//
//		return ResponseEntity.ok(out);
//	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@DeleteMapping("/book-loans/{id}/delete-book-loan")
	public ResponseEntity<LinkedHashMap> deleteBookLoan(@PathVariable("id") long id) {

		LibAppBookLoan libAppBookLoanToDelete = libAppBookLoanRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("Book loan not found"));

		LibAppBookWrapper libAppBookWrapper = libAppBookLoanToDelete.getLoanedBookWrapper();

		if (libAppBookLoanToDelete.getLoanStatus() == LibAppBookLoan.BookLoanStatus.RETURNED) {
			//noop
		} else if (libAppBookLoanToDelete.getLoanStatus() == LibAppBookLoan.BookLoanStatus.LOANED) {
			libAppBookWrapper.increaseAvailableQuantity();
		}

		libAppBookLoanRepository.delete(libAppBookLoanToDelete);

		return ResponseEntity.ok(new LinkedHashMap());
	}
}
