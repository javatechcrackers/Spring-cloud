package com.kellton.userservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ErrorSource {

	@Getter
	@Setter
	private String[] pointer;

	public ErrorSource(String... pointer) {
		super();
		this.pointer = pointer;
	}

	public static ErrorSource getErrorSource(String... pointers) {

		return new ErrorSource(pointers);

	}

}
