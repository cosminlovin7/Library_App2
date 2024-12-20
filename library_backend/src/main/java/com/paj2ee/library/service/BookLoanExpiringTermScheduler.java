package com.paj2ee.library.service;

import com.paj2ee.library.model.LibAppBook;
import com.paj2ee.library.model.LibAppBookLoan;
import com.paj2ee.library.model.LibAppBookWrapper;
import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.repository.LibAppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Component
public class BookLoanExpiringTermScheduler {

	private static final Logger logger = LoggerFactory.getLogger(BookLoanExpiringTermScheduler.class);

	@Autowired
	private LibAppUserRepository libAppUserRepository;
	@Autowired
	private EmailSenderService emailSenderService;

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@Scheduled(cron = "0 0 8 * * ?")//@NOTE: Run daily at 08:00 AM
	public void runBookLoanExpiringTermCheck() {

		logger.info("Book loan expiring term check started...");

		List<LibAppUser> libAppUserLs = libAppUserRepository.findAll();

		LocalDateTime now = LocalDateTime.now();
		for (LibAppUser libAppUser : libAppUserLs) {
			Set<LibAppBookLoan> libAppBookLoanLs = libAppUser.getBookLoans();

			String emailSubject = "[BOOK LOAN] Book loans expiring soon...";
			StringBuilder emailMessageBuilder = new StringBuilder();


			boolean alertsNeeded = false;
			for (LibAppBookLoan libAppBookLoan : libAppBookLoanLs) {

				LibAppBookWrapper libAppBookWrapper = libAppBookLoan.getLoanedBookWrapper();
				LibAppBook libAppBook = libAppBookWrapper.getOwnerLibAppBook();

				String title = libAppBook.getTitle();
				String author = libAppBook.getAuthor();

				LocalDateTime loanExpireOn = libAppBookLoan.getLoanExpireOn();
				long daysDifference = ChronoUnit.DAYS.between(loanExpireOn, now);

				System.out.println(daysDifference);

				if (daysDifference == -1) {
					alertsNeeded = true;
					emailMessageBuilder.append("\r\nBook loan expiring tomorrow for: " + title + " by " + author + "\r\n");
				} else if (daysDifference == 0) {
					alertsNeeded = true;
					emailMessageBuilder.append("\r\nBook loan expiring today for: " + title + " by " + author + "\r\n");
				} else if (daysDifference > 0) {
					alertsNeeded = true;
					emailMessageBuilder.append("\r\nBook loan expired for: " + title + " by " + author + ". Penalties are applied daily." + "\r\n");
				} else {
					//noop
				}

			}

			if (alertsNeeded) {
				emailSenderService.sendSimpleEmail(
					libAppUser.getEmail(),
					emailSubject,
					emailMessageBuilder.toString()
				);
			}

		}

		logger.info("Book loan expiring term check finished!");

	}

}
