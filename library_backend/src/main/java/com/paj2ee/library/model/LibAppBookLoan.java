package com.paj2ee.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(
	name = "libapp_book_loans"
)
public class LibAppBookLoan {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "loaned_on", nullable = false, updatable = true)
	private LocalDateTime loanedOn;

	@Column(name = "loan_expire_on", nullable = false, updatable = true)
	private LocalDateTime loanExpireOn;

	@Column(name = "returned_on", nullable = true, updatable = true)
	private LocalDateTime returnedOn;

	@Enumerated(EnumType.STRING)
	@Column(name = "book_loan_status", nullable = false, updatable = true)
	private BookLoanStatus loanStatus;

	@ManyToOne(optional = false)
	@JoinColumn(name = "loaned_book_wrapper_id", nullable = false, updatable = false)
	private LibAppBookWrapper loanedBookWrapper;

	@ManyToOne(optional = false)
	@JoinColumn(name = "owner_user_id", nullable = false, updatable = false)
	private LibAppUser ownerUser;

	public Long getId() {
		return id;
	}

	public LocalDateTime getLoanedOn() {
		return loanedOn;
	}

	public void setLoanedOn(LocalDateTime loanedOn) {
		this.loanedOn = loanedOn;
	}

	public LocalDateTime getLoanExpireOn() {
		return loanExpireOn;
	}

	public void setLoanExpireOn(LocalDateTime loanExpireOn) {
		this.loanExpireOn = loanExpireOn;
	}

	public LocalDateTime getReturnedOn() {
		return returnedOn;
	}

	public void setReturnedOn(LocalDateTime returnedOn) {
		this.returnedOn = returnedOn;
	}

	public BookLoanStatus getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(BookLoanStatus loanStatus) {
		this.loanStatus = loanStatus;
	}

	public LibAppBookWrapper getLoanedBookWrapper() {
		return loanedBookWrapper;
	}

	public void setLoanedBookWrapper(LibAppBookWrapper loanedBookWrapper) {
		this.loanedBookWrapper = loanedBookWrapper;
	}

	public LibAppUser getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(LibAppUser ownerUser) {
		this.ownerUser = ownerUser;
	}

	public enum BookLoanStatus {
		LOANED,
		RETURNED
	}
}
