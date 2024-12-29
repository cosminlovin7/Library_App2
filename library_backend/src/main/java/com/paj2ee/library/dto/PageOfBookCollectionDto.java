package com.paj2ee.library.dto;

import java.util.List;

public record PageOfBookCollectionDto(
	long total,
	List<BookCollectionDto> ls
) {
}
