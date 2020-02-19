package com.melchenko.springboot_mvc.sweater.controller;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.dto.CaptchaResponseDTO;
import com.melchenko.springboot_mvc.sweater.service.UserService;
import com.melchenko.springboot_mvc.sweater.util.ControllerUtils;

@Controller
public class RegistrationController {
	
	private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

	private final UserService userService;
	
	@Value("${recaptcha.secret}")
	private String secret;
	
	@Autowired 
	private RestTemplate restTemplate;

	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	@PostMapping("/registration")
	public String createUser(@RequestParam("password2") String passwordConfirm,
			@RequestParam("g-recaptcha-response") String captchaResponse, @Valid User user, BindingResult bindingResult,
			Model model) {
		
		String url = String.format(CAPTCHA_URL, secret, captchaResponse);
		CaptchaResponseDTO response = restTemplate.postForObject(url, Collections.EMPTY_LIST, CaptchaResponseDTO.class);
		if (!response.isSuccess()) {
			model.addAttribute("captchaError", "Captcha is not filled");
		}
		
		boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
		if (isConfirmEmpty) {
			model.addAttribute("password2Error", "Password confirmation cannot be empty");
		}

		if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
			model.addAttribute("passwordError", "Passwords are different");
		}

		if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
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
			model.addAttribute("messageType", "success");
			model.addAttribute("message", "User successfully activated");
		} else {
			model.addAttribute("messageType", "danger");
			model.addAttribute("message", "Activation code is not found!");
		}

		return "login";
	}

}
