package com.kellton.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kellton.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findByUsername(String userName);

	User findById(long id);

	boolean existsByUsername(String username);
	
}
