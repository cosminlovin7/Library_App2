package com.paj2ee.library.controller;

import com.paj2ee.library.dto.BookLoanDto;
import com.paj2ee.library.model.LibAppBookLoan;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.repository.LibAppBookLoanRepository;
import com.paj2ee.library.repository.LibAppBookWrapperRepository;
import com.paj2ee.library.repository.LibAppUserRepository;
import com.paj2ee.library.service.BookLoanService;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LibAppUserBookLoanController {

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private LibAppBookWrapperRepository libAppBookWrapperRepository;
	@Autowired
	private LibAppBookLoanRepository libAppBookLoanRepository;

	@Autowired
	private BookLoanService bookLoanService;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@GetMapping("/users/{id}/book-loans")
	public ResponseEntity<List<BookLoanDto>> getBookLoans(
		@PathVariable(name = "id") long userId,
		@RequestParam(name = "loanStatus", required = false) List<LibAppBookLoan.BookLoanStatus> loanStatusLs
	) {

		Specification<LibAppBookLoan> allSpec = rootSpec();

		allSpec = allSpec.and(compareWithUserIdSpec(userId));

		if (null != loanStatusLs) {
			Specification <LibAppBookLoan> loanStatusSpec = null;

			for (LibAppBookLoan.BookLoanStatus loanStatus : loanStatusLs) {
				if (null == loanStatusSpec) {
					loanStatusSpec = compareWithLoanStatusSpec(loanStatus);
				} else {
					loanStatusSpec = loanStatusSpec.or(compareWithLoanStatusSpec(loanStatus));
				}
			}

			if (null != loanStatusSpec) {
				allSpec = allSpec.and(loanStatusSpec);
			}
		}

		List<LibAppBookLoan> libAppBookLoanLs = libAppBookLoanRepository.findAll(allSpec);

		List<BookLoanDto> outLs = new ArrayList<>();
		for (LibAppBookLoan libAppBookLoan : libAppBookLoanLs) {
			outLs.add(BookLoanDto.fromEntity(libAppBookLoan));
		}

		return ResponseEntity.ok(outLs);

	}

	private static Specification<LibAppBookLoan> rootSpec() {
		return (root, query, builder) -> {
			return builder.conjunction();
		};
	}

	private static Specification<LibAppBookLoan> compareWithUserIdSpec(long userId) {
		return (root, query, builder) -> {
			Join<LibAppBookLoan, LibAppUser> child = root.join("ownerUser");

			return builder.equal(child.get("id"), userId);
		};
	}

	private static Specification<LibAppBookLoan> compareWithLoanStatusSpec(LibAppBookLoan.BookLoanStatus loanStatus) {
		return (root, query, builder) -> {
			return builder.equal(root.get("loanStatus"), loanStatus);
		};
	}

}
