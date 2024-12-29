package com.paj2ee.library.dto;

import java.util.List;

public record UserInfoDto (
	Long id,
	String username,
	String email,
	String phoneNumber,
	boolean enabled,
	List<String> authorities,
	FileInfoDto identityPhotoFile
) {
}
