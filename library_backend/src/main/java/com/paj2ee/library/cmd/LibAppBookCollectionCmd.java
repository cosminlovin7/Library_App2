package com.paj2ee.library.cmd;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppBookCollectionCmd(
	@NotNull(message = "Invalid collection name. Collection name cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid collection name. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*{}|<>]+$",
		message = "Invalid collection name. Collection name cannot contain special characters."
	)
	String name
) {
}
