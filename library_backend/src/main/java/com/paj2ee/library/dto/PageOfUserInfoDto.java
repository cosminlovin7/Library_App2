package com.paj2ee.library.dto;

import java.util.List;

public record PageOfUserInfoDto(
	long total,
	List<UserInfoDto> ls
) {
}
