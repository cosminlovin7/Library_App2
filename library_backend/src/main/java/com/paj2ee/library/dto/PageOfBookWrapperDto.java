package com.paj2ee.library.dto;

import java.util.List;

public record PageOfBookWrapperDto(
	long total,
	List<BookWrapperDto> ls
) {
}
