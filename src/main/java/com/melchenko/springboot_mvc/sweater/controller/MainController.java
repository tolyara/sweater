package com.melchenko.springboot_mvc.sweater.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.melchenko.springboot_mvc.sweater.domain.Message;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.reps.MessageRepo;

@Controller
public class MainController {

	private MessageRepo messageRepo;

	@Autowired
	public MainController(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	@GetMapping("/")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "User") String name,
			Map<String, Object> model) {
		model.put("name", name);
		return "greeting";
	}

	@GetMapping("/main")
	public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
		Iterable<Message> messages = messageRepo.findAll();
		
		if (filter != null && !filter.isEmpty()) {
			messages = messageRepo.findByTag(filter);
		} else {
			messages = messageRepo.findAll();
		}
		
//		model.put("messages", messages);
		model.addAttribute("messages", messages);
		model.addAttribute("filter", filter);
		return "main";
	}

	@PostMapping("/main")
	public String addMessage(@AuthenticationPrincipal User user, @RequestParam String text, @RequestParam String tag,
			Map<String, Object> model) {
		Message message = new Message(text, tag, user);
		messageRepo.save(message);

		Iterable<Message> messages = messageRepo.findAll();
		model.put("messages", messages);
		return "main";
	}

//	@PostMapping("filter")
//	public String filter(@RequestParam String filter, Map<String, Object> model) {
//		Iterable<Message> messages;
//
//		if (filter != null && !filter.isEmpty()) {
//			messages = messageRepo.findByTag(filter);
//		} else {
//			messages = messageRepo.findAll();
//		}
//
//		model.put("messages", messages);
//		return "main";
//	}

}
