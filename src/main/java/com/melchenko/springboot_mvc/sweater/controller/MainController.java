package com.melchenko.springboot_mvc.sweater.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.melchenko.springboot_mvc.sweater.domain.Message;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.reps.MessageRepo;

@Controller
public class MainController {

	private MessageRepo messageRepo;

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	public MainController(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
//		model.put("name", name);
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
			Map<String, Object> model, @RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {
		Message message = new Message(text, tag, user);

		if (file != null && !file.getOriginalFilename().isEmpty()) {
			File uploadDirectory = new File(uploadPath);
			if (!uploadDirectory.exists()) {
				uploadDirectory.mkdir();
			}
			String uuidFile = UUID.randomUUID().toString();
			String resultFilename = uuidFile + "." + file.getOriginalFilename();
			file.transferTo(new File(uploadPath + "/" + resultFilename));
			message.setFilename(resultFilename);
		}

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
