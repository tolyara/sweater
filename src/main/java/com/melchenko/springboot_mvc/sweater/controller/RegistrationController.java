package com.melchenko.springboot_mvc.sweater.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.melchenko.springboot_mvc.sweater.domain.Role;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.reps.UserRepo;

@Controller
public class RegistrationController {

	private UserRepo userRepo;

	@Autowired
	public RegistrationController(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	@PostMapping("/registration")
	public String createUser(User user, Map<String, Object> model) {
		User userFromDB = userRepo.findByUsername(user.getUsername());
		
		if (userFromDB != null) {
			model.put("messageIfExists", "User exists!");
			return "registration";
		}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		userRepo.save(user);
		return "redirect:/login";
	}

}
