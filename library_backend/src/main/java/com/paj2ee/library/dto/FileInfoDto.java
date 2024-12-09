package com.paj2ee.library.dto;

public record FileInfoDto(
	FileInfoMetaDto meta,
	String base64FileContent
) {

}
