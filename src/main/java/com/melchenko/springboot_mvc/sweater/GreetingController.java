package com.melchenko.springboot_mvc.sweater;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.melchenko.springboot_mvc.sweater.domain.Message;
import com.melchenko.springboot_mvc.sweater.reps.MessageRepo;

@Controller
public class GreetingController {

	private MessageRepo messageRepo;

	@Autowired
	public GreetingController(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	@GetMapping("/")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "User") String name,
			Map<String, Object> model) {
		model.put("name", name);
		return "greeting";
	}

	@GetMapping("/main")
	public String main(Map<String, Object> model) {
		Iterable<Message> messages = messageRepo.findAll();
		model.put("messages", messages);
		return "main";
	}

	@PostMapping("/main")
	public String addMessage(@RequestParam String text, @RequestParam String tag) {
		Message message = new Message(text, tag);
		messageRepo.save(message);

//		Iterable<Message> messages = messageRepo.findAll();
//		model.put("messages", messages);

		return "redirect:/";
//		return "main";
	}

	@PostMapping("filter")
	public String filter(@RequestParam String filter, Map<String, Object> model) {
		Iterable<Message> messages;
		
		if (filter != null && !filter.isEmpty()) {
			messages = messageRepo.findByTag(filter);
		} else {
			messages = messageRepo.findAll();
		}
		
		model.put("messages", messages);
		return "main";
	}

}
