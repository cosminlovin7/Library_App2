package com.paj2ee.library.cmd;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LibAppCreateBookWrapperCmd(
	@NotNull(message = "Invalid owner book id. Owner book id cannot be null.")
	Long ownerLibAppBookId,

	@NotNull(message = "Invalid quantity. Quantity cannot be null.")
	@Min(0)
	@Max(9999)
	Integer quantity,

	@NotNull(message = "Invalid available quantity. Available quantity cannot be null.")
	@Min(0)
	@Max(9999)
	Integer availableQuantity
) {
}
