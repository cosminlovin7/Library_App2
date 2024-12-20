package com.paj2ee.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Set;

@Entity
@Table(
	name = "libapp_book_collection",
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"name"}
		)
	}
)
public class LibAppBookCollection {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false, updatable = true, length = 50)
	private String name;

	@OneToMany(
		fetch = FetchType.LAZY,
		mappedBy = "collection"
	)
	private Set<LibAppBook> bookSet;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<LibAppBook> getBookSet() {
		return bookSet;
	}
}
