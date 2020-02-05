package com.kellton.userservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kellton.userservice.domain.User;
import com.kellton.userservice.dto.UserDTO;
import com.kellton.userservice.exception.ValidationException;
import com.kellton.userservice.repository.UserRepository;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

	@Autowired
	private UserRepository usersRepository;

    @GetMapping("/")
    public List<User> getAllUser() {
        return  usersRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable(value = "id") String id) {
    	System.out.println("id " +id);
        return  usersRepository.findById(Long.parseLong(id));
    }
    
    @PostMapping("/")
    public List<User> addUser(@RequestBody @Valid UserDTO user) {
    	if (usersRepository.existsByUsername(user.getUsername())){
            throw new ValidationException("Username already existed");
        }
    	
    	String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    	User u = new User();
    	u.setId(user.getId());
    	u.setUsername(user.getUsername());
    	u.setPassword(encodedPassword);
    	u.setFullname(user.getFullname());
    	u.setMobile(user.getMobile());
    	u.setEmail(user.getEmail());
        usersRepository.save(u);
        return  usersRepository.findAll();
    }
	
    @PutMapping("/")
    public List<User> updateUser(@RequestBody @Valid UserDTO user) {
    	String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    	User u = new User();
    	u.setId(user.getId());
    	u.setUsername(user.getUsername());
    	u.setPassword(encodedPassword);
    	u.setFullname(user.getFullname());
    	u.setMobile(user.getMobile());
    	u.setEmail(user.getEmail());
        usersRepository.save(u);
        return  usersRepository.findAll();
    }
    
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable String id){
        usersRepository.deleteById(Long.parseLong(id));
        return true;
    }
    
}
