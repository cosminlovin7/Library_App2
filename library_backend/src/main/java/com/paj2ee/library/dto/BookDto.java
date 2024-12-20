package com.paj2ee.library.dto;

import com.paj2ee.library.model.LibAppBook;

public record BookDto(
	Long id,
	String title,
	String author,
	String editure,
	BookCollectionDto collection,
	short edition,
	String isbn,
	String translator,
	int pageNr,
	String bookFormat,
	LibAppBook.BookCoverType coverType,
	int yearOfPublication
) {

	public static BookDto fromEntity(LibAppBook libAppBook) {
		if (null == libAppBook) return null;

		return new BookDto(
			libAppBook.getId(),
			libAppBook.getTitle(),
			libAppBook.getAuthor(),
			libAppBook.getEditure(),
			BookCollectionDto.fromEntity(libAppBook.getCollection()),
			libAppBook.getEdition(),
			libAppBook.getIsbn(),
			libAppBook.getTranslator(),
			libAppBook.getPageNr(),
			libAppBook.getBookFormat(),
			libAppBook.getCoverType(),
			libAppBook.getYearOfPublication()
		);
	}

}
