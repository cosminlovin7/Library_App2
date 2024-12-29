package com.paj2ee.library.dto;

import java.util.List;

public record PageOfBookDto(
	long total,
	List<BookDto> ls
) {
}
