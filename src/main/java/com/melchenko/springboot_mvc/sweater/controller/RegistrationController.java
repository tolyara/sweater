package com.melchenko.springboot_mvc.sweater.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.service.UserService;
import com.melchenko.springboot_mvc.sweater.util.ControllerUtils;

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
	public String createUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
			model.addAttribute("passwordError", "Passwords are different");
		}
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errors);
			return "registration";
		}
		
		if (!userService.addUser(user)) {
			model.addAttribute("usernameError", "User exists!");
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
