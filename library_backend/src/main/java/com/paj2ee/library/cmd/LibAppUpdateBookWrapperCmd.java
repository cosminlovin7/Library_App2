package com.paj2ee.library.cmd;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LibAppUpdateBookWrapperCmd (
	@NotNull(message = "Invalid quantity. Quantity cannot be null.")
	@Min(0)
	@Max(9999)
	Integer quantity,

	@NotNull(message = "Invalid available quantity. Available quantity cannot be null.")
	@Min(0)
	@Max(9999)
	Integer availableQuantity
){
}
