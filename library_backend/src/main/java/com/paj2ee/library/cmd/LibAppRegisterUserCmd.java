package com.paj2ee.library.cmd;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppRegisterUserCmd(
	@NotNull(message = "Invalid username. Username cannot be null.")
	@Length(min = 8, max = 20)
	@Pattern(
		regexp = "^[!@#$%^&*(),.?\":{}|<>]+$",
		message = "Invalid username. Username cannot contain special characters."
	)
	String username,

	@NotNull(message = "Invalid password. Password cannot be null.")
	@Length(
		min = 8,
		max = 20,
		message = "Invalid password. Must have min. 8 and max. 20 characters."
	)
	String password,

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
