package com.paj2ee.library.service;

import com.paj2ee.library.model.LibAppBookLoan;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.repository.LibAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class BookLoanServiceImpl implements BookLoanService {

	private static final double taxPerDay = 0.5;

	@Autowired
	private LibAppUserRepository libAppUserRepository;

	public boolean isUserEligibleForBookLoan(long id) {

		LibAppUser libAppUserToCheck = libAppUserRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));

		Set<LibAppBookLoan> libAppBookLoanLs = libAppUserToCheck.getBookLoans();

		LocalDateTime now = LocalDateTime.now();
		int daysPenalty = 0;
		for (LibAppBookLoan libAppBookLoan : libAppBookLoanLs) {
			LocalDateTime loanExpireOn = libAppBookLoan.getLoanExpireOn();

			if (loanExpireOn.isBefore(now)) {
				daysPenalty += (int) ChronoUnit.DAYS.between(loanExpireOn, now);
			}
		}

		if (daysPenalty > 0) {
			return false;
		}

		return true;

	}

	public double calculateBookLoanTaxForUser(long id) {

		LibAppUser libAppUserToCheck = libAppUserRepository
			.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));

		Set<LibAppBookLoan> libAppBookLoanLs = libAppUserToCheck.getBookLoans();

		LocalDateTime now = LocalDateTime.now();
		int daysPenalty = 0;
		for (LibAppBookLoan libAppBookLoan : libAppBookLoanLs) {
			LocalDateTime loanExpireOn = libAppBookLoan.getLoanExpireOn();

			if (loanExpireOn.isBefore(now)) {
				daysPenalty += (int) ChronoUnit.DAYS.between(loanExpireOn, now);
			}
		}

		return daysPenalty * taxPerDay;

	}

}
