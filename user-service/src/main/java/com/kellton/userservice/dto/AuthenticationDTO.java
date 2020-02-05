package com.kellton.userservice.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
@Setter @Getter
public class AuthenticationDTO {

	@NotBlank(message = "Username can not be blank.")
	private String username;
	@NotBlank(message = "Password can not be blank.")
	private String password;

}
