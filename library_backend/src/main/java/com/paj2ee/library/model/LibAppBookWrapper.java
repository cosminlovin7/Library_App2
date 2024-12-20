package com.paj2ee.library.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "libapp_book_wrappers",
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"owner_libapp_book_id"}
		)
	}
)
public class LibAppBookWrapper {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@OneToOne(
		optional = false,
		cascade = {
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH
		},
		fetch = FetchType.EAGER
	)
	@JoinColumn(name = "owner_libapp_book_id", nullable = false, unique = true)
	private LibAppBook ownerLibAppBook;

	@Column(name = "quantity", nullable = false, updatable = true)
	private int quantity;

	@Column(name = "available_quantity", nullable = false, updatable = true)
	private int availableQuantity;

	public Long getId() {
		return id;
	}

	public LibAppBook getOwnerLibAppBook() {
		return ownerLibAppBook;
	}

	public void setOwnerLibAppBook(LibAppBook ownerLibAppBook) {
		this.ownerLibAppBook = ownerLibAppBook;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public void decreaseAvailableQuantity() {
		this.availableQuantity--;
	}

	public void increaseAvailableQuantity() {
		this.availableQuantity++;
	}
}
