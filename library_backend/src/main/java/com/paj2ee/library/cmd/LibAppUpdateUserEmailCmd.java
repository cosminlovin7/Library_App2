package com.paj2ee.library.cmd;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppUpdateUserEmailCmd(
	@NotNull(message = "Invalid email. Email cannot be null.")
	@Length(
		min = 8,
		max = 30,
		message = "Invalid email. Must have min. 8 and max. 30 characters."
	)
	@Pattern(
		regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
		message = "Invalid email. Email has wrong format."
	)
	String email
) {
}
