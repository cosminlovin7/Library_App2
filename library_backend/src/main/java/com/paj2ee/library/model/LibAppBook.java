package com.paj2ee.library.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "libapp_books",
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"title", "author", "editure"}
		)
	}
)
public class LibAppBook {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "title", nullable = false, updatable = true, length = 50)
	private String title;

	@Column(name = "author", nullable = false, updatable = true, length = 50)
	private String author;

	@Column(name = "editure", nullable = false, updatable = true, length = 50)
	private String editure;

	@ManyToOne(optional = true)
	private LibAppBookCollection collection;

	@Column(name = "edition", nullable = false, updatable = true)
	private short edition;

	@Column(name = "isbn", nullable = false, updatable = true, length = 17)
	private String isbn;

	@Column(name = "translator", nullable = false, updatable = true, length = 50)
	private String translator;

	@Column(name = "page_nr", nullable = false, updatable = true)
	private int pageNr;

	@Column(name = "book_format", nullable = false, updatable = true, length = 50)
	private String bookFormat;

	@Enumerated(EnumType.STRING)
	@Column(name = "cover_type", nullable = false, updatable = true, length = 50)
	private BookCoverType coverType;

	@Column(name = "year_of_publication", nullable = false, updatable = true)
	private int yearOfPublication;

	@OneToOne(
		mappedBy = "ownerLibAppBook",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		optional = true,
		fetch = FetchType.EAGER
	)
	private LibAppBookWrapper libAppBookWrapper;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEditure() {
		return editure;
	}

	public void setEditure(String editure) {
		this.editure = editure;
	}

	public LibAppBookCollection getCollection() {
		return collection;
	}

	public void setCollection(LibAppBookCollection collection) {
		this.collection = collection;
	}

	public short getEdition() {
		return edition;
	}

	public void setEdition(short edition) {
		this.edition = edition;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public int getPageNr() {
		return pageNr;
	}

	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	public String getBookFormat() {
		return bookFormat;
	}

	public void setBookFormat(String bookFormat) {
		this.bookFormat = bookFormat;
	}

	public BookCoverType getCoverType() {
		return coverType;
	}

	public void setCoverType(BookCoverType coverType) {
		this.coverType = coverType;
	}

	public int getYearOfPublication() {
		return yearOfPublication;
	}

	public void setYearOfPublication(int yearOfPublication) {
		this.yearOfPublication = yearOfPublication;
	}

	public LibAppBookWrapper getLibAppBookWrapper() {
		return libAppBookWrapper;
	}

	public void setLibAppBookWrapper(LibAppBookWrapper libAppBookWrapper) {
		this.libAppBookWrapper = libAppBookWrapper;
	}

	public enum BookCoverType {
		BROSATA,
		CARTONATA,
		PLASTIC,
	}
}
