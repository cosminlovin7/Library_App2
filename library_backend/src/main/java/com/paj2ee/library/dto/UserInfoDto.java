package com.paj2ee.library.dto;

import java.util.List;

public record UserInfoDto (
	Long id,
	String username,
	boolean enabled,
	List<String> authorities,
	FileInfoDto identityPhotoFile
) {
}
