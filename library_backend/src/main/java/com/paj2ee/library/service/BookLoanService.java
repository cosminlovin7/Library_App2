package com.paj2ee.library.service;

public interface BookLoanService {

	boolean isUserEligibleForBookLoan(long id);
	double calculateBookLoanTaxForUser(long id);

}
