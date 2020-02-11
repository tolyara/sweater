package com.melchenko.springboot_mvc.sweater.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.service.UserService;

@Controller
public class RegistrationController {

	private UserService userService;

	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	@PostMapping("/registration")
	public String createUser(User user, Map<String, Object> model) {
		if (!userService.addUser(user)) {
			model.put("message", "User exists!");
			return "registration";
		}
			
		return "redirect:/login";	
	}
	
	@GetMapping("/activate/{code}")
	public String activateUser(Model model, @PathVariable String code) {
		boolean isActivated = userService.activateUser(code);
		
		if (isActivated) {
			model.addAttribute("message", "User successfully activated");
		} else {
			model.addAttribute("message", "Activation code is not found!");
		}
		
		return "login";
	}
	
	
	
	

}
