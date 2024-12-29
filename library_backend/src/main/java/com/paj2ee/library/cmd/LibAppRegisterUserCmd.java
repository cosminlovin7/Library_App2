package com.paj2ee.library.cmd;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppRegisterUserCmd(
	@NotNull(message = "Invalid username. Username cannot be null.")
	@Length(
		min = 8,
		max = 30,
		message = "Invalid username. Must have min. 8 and max. 30 characters."
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
	String password,

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
	String email,

	@NotNull(message = "Invalid phone number. Phone number cannot be null.")
	@Length(
		min = 10,
		max = 10,
		message = "Invalid phone number. Must have 10 characters.")
	@Pattern(
		regexp = "^\\d{10}$",
		message = "Invalid phone number. Phone number can contain only digits."
	)
	String phoneNumber,

	@NotNull
	@Pattern(
		regexp = "^[^!@#$%^&*(),?\":{}|<>]+$",
		message = "Invalid file name. File name cannot contain special characters."
	)
	String fileName,

	@NotNull
	@Max(5 * 1024 * 1024)
	Long fileSize,

	@NotNull
	@Pattern(
		regexp = "^[^!@#$%^&*(),.?\":{}|<>]+$",
		message = "Invalid file type. File type cannot contain special characters."
	)
	String fileType,

	@NotNull(message = "Invalid file. File cannot be null.")
	String fileData
) {
}
