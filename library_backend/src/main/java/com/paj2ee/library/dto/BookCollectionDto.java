package com.paj2ee.library.dto;

import com.paj2ee.library.model.LibAppBookCollection;

public record BookCollectionDto(
	long id,
	String name
) {

	public static BookCollectionDto fromEntity(LibAppBookCollection libAppBookCollection) {
		if (null == libAppBookCollection) {
			return null;
		}

		return new BookCollectionDto(
			libAppBookCollection.getId(),
			libAppBookCollection.getName()
		);
	}

}
