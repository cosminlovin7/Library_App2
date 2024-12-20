package com.paj2ee.library.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(
	name = "libapp_users",
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"username"}
		)
	}
)
public class LibAppUser {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "username", nullable = false, updatable = false)
	private String username;

	@Column(name = "password", nullable = false, updatable = true)
	private String password;

	@Column(name = "email", nullable = false, updatable = true)
	private String email;

	@Column(name = "phone_number", nullable = false, updatable = true)
	private String phoneNumber;

	@Column(name = "enabled", nullable = false, updatable = true)
	private boolean enabled;

	@OneToMany(
		mappedBy = "ownerUser",
	  	cascade = CascadeType.ALL,
		fetch = FetchType.EAGER
	)
	private Set<LibAppUserAuthority> authorities;

	@OneToMany(
		mappedBy = "ownerUser",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY
	)
	private Set<LibAppBookLoan> bookLoans;

	@OneToOne(
		optional = true
	)
	private LibAppFile identityCardFile;

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<LibAppUserAuthority> getAuthorities() {
		return authorities;
	}

	public LibAppFile getIdentityCardFile() {
		return identityCardFile;
	}

	public void setIdentityCardFile(LibAppFile identityCardFile) {
		this.identityCardFile = identityCardFile;
	}

	public Set<LibAppBookLoan> getBookLoans() {
		return bookLoans;
	}

	public void setBookLoans(Set<LibAppBookLoan> bookLoans) {
		this.bookLoans = bookLoans;
	}

	@Override
	public String toString() {
		return "LibAppUser{" +
				   "id=" + id +
				   ", username='" + username + '\'' +
				   ", password='" + password + '\'' +
				   ", phoneNumber='" + phoneNumber + '\'' +
				   ", enabled=" + enabled +
				   ", authorities=" + authorities +
				   '}';
	}
}
