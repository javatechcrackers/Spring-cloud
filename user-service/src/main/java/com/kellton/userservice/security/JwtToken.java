package com.kellton.userservice.security;

public class JwtToken {

	private String value;

	public JwtToken(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
