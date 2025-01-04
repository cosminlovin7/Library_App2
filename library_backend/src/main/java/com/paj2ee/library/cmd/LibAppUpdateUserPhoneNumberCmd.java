package com.paj2ee.library.cmd;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppUpdateUserPhoneNumberCmd(
	@NotNull(message = "Invalid phone number. Phone number cannot be null.")
	@Length(
		min = 10,
		max = 10,
		message = "Invalid phone number. Must have 10 characters.")
	@Pattern(
		regexp = "^\\d{10}$",
		message = "Invalid phone number. Phone number can contain only digits."
	)
	String phoneNumber
) {
}
