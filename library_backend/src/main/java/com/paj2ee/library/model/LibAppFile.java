package com.paj2ee.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "libapp_files",
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"filename"}
		)
	}
)
public class LibAppFile {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "filename", nullable = false, updatable = false)
	private String filename;

	@Column(name = "size", nullable = false, updatable = false)
	private Long size;

	@Column(name = "type", nullable = false, updatable = false)
	private String type;

	@Column(name = "status", nullable = false, updatable = true)
	private String status;

	public Long getId() {
		return id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LibAppFile{" +
			"id=" + id +
			", filename='" + filename + '\'' +
			", size=" + size +
			", type='" + type + '\'' +
			", status='" + status + '\'' +
			'}';
	}
}
