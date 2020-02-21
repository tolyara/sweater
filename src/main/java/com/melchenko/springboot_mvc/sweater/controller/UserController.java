package com.melchenko.springboot_mvc.sweater.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.melchenko.springboot_mvc.sweater.domain.Role;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.service.UserService;

@Controller
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public String userList(Model model) {
		model.addAttribute("users", userService.findAll());
		return "userList";
	}

	@GetMapping("{user}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String userEditForm(@PathVariable User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("roles", Role.values());
		return "userEdit";
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public String userSave(@RequestParam String userName, @RequestParam Map<String, String> form,
			@RequestParam("userId") User user) {
		userService.saveUser(user, userName, form);
		return "redirect:/user";
	}

	@GetMapping("profile")
	public String getProfile(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("username", user.getUsername());
		model.addAttribute("email", user.getEmail());
		return "profile";
	}

	@PostMapping("profile")
	public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String password, @RequestParam String email) {
		userService.updateUserProfile(user, password, email);
		return "redirect:/user/profile";
	}
	
	@GetMapping("subscribe/{user}")
	public String subscribe(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
		userService.subscribe(currentUser, user);
		return "redirect:/user-messages/" + user.getId();
	}
	
	@GetMapping("unsubscribe/{user}")
	public String unsubscribe(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
		userService.unsubscribe(currentUser, user);
		return "redirect:/user-messages/" + user.getId();
	}
	
	@GetMapping("{type}/{user}/list")
//	@GetMapping("subscribers/1/list")
	public String userSubscriptionsList(Model model, @PathVariable User user, @PathVariable String type) {
		model.addAttribute("userChannel", user);
		model.addAttribute("type", type);
		
		if ("subscriptions".equals(type)) {
			model.addAttribute("users", user.getSubscriptions());
		}
		else if ("subscribers".equals(type)) {
			model.addAttribute("users", user.getSubscribers());
		}
		else {
			throw new RuntimeException("Cannot define subscription type!");
		}

		return "subscriptions"; // must be correct else 404 will be throwed
	}
	
	
	
	
	
	

}
