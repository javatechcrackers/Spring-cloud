package com.kellton.userservice.controller;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kellton.userservice.domain.User;
import com.kellton.userservice.dto.AuthenticationDTO;
import com.kellton.userservice.exception.ValidationException;
import com.kellton.userservice.repository.UserRepository;
import com.kellton.userservice.security.AuthFree;
import com.kellton.userservice.security.JWTTokenService;

@RestController
@RequestMapping("/login")
@Validated
public class AuthenticationController {

	@Autowired
	private UserRepository usersRepository;
	
	@Autowired 
	JWTTokenService jwtTokenService;

	@PostMapping("/")
	@AuthFree
	public String login(@RequestBody @Valid AuthenticationDTO user) throws SQLException, Exception {
		User u = null;
		List<User> userList = usersRepository.findByUsername(user.getUsername());

		if (userList.size() > 0) {
			u = userList.get(0);
			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
			boolean isPasswordMatches = bcrypt.matches(user.getPassword(), u.getPassword());

			if (u.getUsername().equalsIgnoreCase(user.getUsername()) && isPasswordMatches) {
				return jwtTokenService.createToken(user.getUsername());
			}
		}else {
			 throw new ValidationException("Username not exist");
		}
		return null;

	}

}
