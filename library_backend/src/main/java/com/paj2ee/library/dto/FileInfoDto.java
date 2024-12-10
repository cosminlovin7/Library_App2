package com.paj2ee.library.dto;

public record FileInfoDto(
	Long id,
	FileInfoMetaDto meta,
	String base64FileContent
) {

}
