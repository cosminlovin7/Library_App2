package com.paj2ee.library.cmd;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppLoginUserCmd(
	@NotNull(message = "Invalid username. Username cannot be null.")
	@Length(
		min = 8,
		max = 30,
		message = "Invalid password. Must have min. 8 and max. 30 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*(),.?\":{}|<>]+$",
		message = "Invalid username. Username cannot contain special characters."
	)
	String username,

	@NotNull(message = "Invalid password. Password cannot be null.")
	@Length(
		min = 8,
		max = 30,
		message = "Invalid password. Must have min. 8 and max. 30 characters."
	)
	String password
) {
}
