package com.melchenko.springboot_mvc.sweater.reps;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melchenko.springboot_mvc.sweater.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

	User findByActivationCode(String code);

}
