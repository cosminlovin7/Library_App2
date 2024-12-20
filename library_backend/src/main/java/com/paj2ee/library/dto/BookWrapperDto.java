package com.paj2ee.library.dto;

import com.paj2ee.library.model.LibAppBookWrapper;

public record BookWrapperDto(
	Long id,
	BookDto book,
	int quantity,
	int availableQuantity
) {

	public static BookWrapperDto fromEntity(LibAppBookWrapper libAppBookWrapper) {

		if (null == libAppBookWrapper) {
			return null;
		}

		return new BookWrapperDto(
			libAppBookWrapper.getId(),
			BookDto.fromEntity(libAppBookWrapper.getOwnerLibAppBook()),
			libAppBookWrapper.getQuantity(),
			libAppBookWrapper.getAvailableQuantity()
		);

	}

}
