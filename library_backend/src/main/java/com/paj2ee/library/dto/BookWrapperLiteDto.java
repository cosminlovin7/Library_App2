package com.paj2ee.library.dto;

import com.paj2ee.library.model.LibAppBookWrapper;

public record BookWrapperLiteDto (
	Long id,
	int quantity,
	int availableQuantity
) {

	public static BookWrapperLiteDto fromEntity(LibAppBookWrapper libAppBookWrapper) {

		if (null == libAppBookWrapper) {
			return null;
		}

		return new BookWrapperLiteDto(
			libAppBookWrapper.getId(),
			libAppBookWrapper.getQuantity(),
			libAppBookWrapper.getAvailableQuantity()
		);

	}

}
