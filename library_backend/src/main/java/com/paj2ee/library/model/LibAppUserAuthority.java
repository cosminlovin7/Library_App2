package com.paj2ee.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
	name = "libapp_user_authorities"
)
public class LibAppUserAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "authority", nullable = false, updatable = false)
	private String authority;

	@ManyToOne(
		optional = false
	)
	private LibAppUser ownerUser;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public LibAppUser getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(LibAppUser ownerUser) {
		this.ownerUser = ownerUser;
	}

	@Override
	public String toString() {
		return "LibAppUserAuthority{" +
				   "id=" + id +
				   ", authority='" + authority + '\'' +
				   '}';
	}
}
