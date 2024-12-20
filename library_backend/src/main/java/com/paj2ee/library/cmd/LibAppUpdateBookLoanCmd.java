package com.paj2ee.library.cmd;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LibAppUpdateBookLoanCmd(
	@NotNull(message = "LoanedOn is invalid. LoanedOn cannot be null.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime loanedOn,

	@NotNull(message = "LoanExpireOn is invalid. LoanExpireOn on cannot be null.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime loanExpireOn,

	@NotNull(message = "LoanedBookWrapperId is invalid. LoanedBookWrapperId on cannot be null.")
	Long loanedBookWrapperId,

	@NotNull(message = "OwnerUserId is invalid. OwnerUserId on cannot be null.")
	Long ownerUserId
) {
}
