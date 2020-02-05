package com.kellton.userservice.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO extends AuthenticationDTO {

	private Long id;
	@Email(message = "Please provide valid email.")
	private String email;
	private String fullname;
	@Pattern(regexp = "(^$|[0-9]{10})", message = "please enter 10 digits phone number.")
	private String mobile;

}
