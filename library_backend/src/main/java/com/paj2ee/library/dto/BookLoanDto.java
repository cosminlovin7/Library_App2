package com.paj2ee.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paj2ee.library.model.LibAppBookLoan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookLoanDto(
	long id,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime loanedOn,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime loanExpireOn,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime returnedOn,
	BigDecimal loanFineAmount,
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
			libAppBookLoan.getLoanFineAmount(),
			libAppBookLoan.getLoanStatus(),
			BookWrapperDto.fromEntity(libAppBookLoan.getLoanedBookWrapper()),
			null != libAppBookLoan.getOwnerUser() ? libAppBookLoan.getOwnerUser().getUsername() : null
		);

	}

}
