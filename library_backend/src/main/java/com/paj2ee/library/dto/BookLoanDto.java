package com.paj2ee.library.dto;

import com.paj2ee.library.model.LibAppBookLoan;

import java.time.LocalDateTime;

public record BookLoanDto(
	long id,
	LocalDateTime loanedOn,
	LocalDateTime loanExpireOn,
	LocalDateTime returnedOn,
	LibAppBookLoan.BookLoanStatus loanStatus,
	BookWrapperDto bookWrapper,
	String user
) {

	public static BookLoanDto fromEntity(LibAppBookLoan libAppBookLoan) {

		if (null == libAppBookLoan) {
			return null;
		}

		return new BookLoanDto(
			libAppBookLoan.getId(),
			libAppBookLoan.getLoanedOn(),
			libAppBookLoan.getLoanExpireOn(),
			libAppBookLoan.getReturnedOn(),
			libAppBookLoan.getLoanStatus(),
			BookWrapperDto.fromEntity(libAppBookLoan.getLoanedBookWrapper()),
			null != libAppBookLoan.getOwnerUser() ? libAppBookLoan.getOwnerUser().getUsername() : null
		);

	}

}
