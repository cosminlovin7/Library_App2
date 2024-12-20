package com.paj2ee.library.cmd;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LibAppUpdateBookCmd(
	@NotNull(message = "Invalid title. Title cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid title. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*{}|<>]+$",
		message = "Invalid title. Title cannot contain special characters."
	)
	String title,

	@NotNull(message = "Invalid author. Author cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid author. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*{}|<>]+$",
		message = "Invalid author. Author cannot contain special characters."
	)
	String author,

	@NotNull(message = "Invalid editure. Editure cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid editure. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*{}|<>]+$",
		message = "Invalid editure. Editure cannot contain special characters."
	)
	String editure,

	@NotNull(message = "Invalid edition. Edition cannot be null.")
	@Min(0)
	@Max(255)
	Short edition,

	@NotNull(message = "Invalid isbn. ISBN cannot be null.")
	@Length(
		min = 17,
		max = 17,
		message = "Invalid isbn. Must have 17 characters."
	)
	@Pattern(
		regexp = "^\\d{3}-\\d{3}-\\d{3}-\\d{3}-\\d{1}$",
		message = "Invalid isbn. ISBN can only contain decimals and hyphens."
	)
	String isbn,

	@NotNull(message = "Invalid translator. Translator cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid translator. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*{}|<>]+$",
		message = "Invalid translator. Translator cannot contain special characters."
	)
	String translator,

	@NotNull(message = "Invalid edition. Edition cannot be null.")
	@Min(0)
	@Max(1000)
	Integer pageNr,

	@NotNull(message = "Invalid book format. Book format cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid book format. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^[^!@#$%^&*{}|<>]+$",
		message = "Invalid book format. Book format can only contain decimals and hyphens."
	)
	String bookFormat,

	@NotNull(message = "Invalid cover type. Cover type cannot be null.")
	@Length(
		min = 0,
		max = 50,
		message = "Invalid cover type. Must have min. 0 and max. 50 characters."
	)
	@Pattern(
		regexp = "^(BROSATA|CARTONATA|PLASTIC)$",
		message = "Invalid cover type. Cover type can only be specific."
	)
	String coverType,

	@NotNull(message = "Invalid year of publication. Year of publication cannot be null.")
	@Min(1900)
	@Max(9999)
	Integer yearOfPublication
) {
}