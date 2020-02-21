package com.melchenko.springboot_mvc.sweater.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.melchenko.springboot_mvc.sweater.domain.Message;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.reps.MessageRepo;
import com.melchenko.springboot_mvc.sweater.util.ControllerUtils;

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

		model.addAttribute("messages", messages);
		model.addAttribute("filter", filter);
		return "main";
	}

	@PostMapping("/main")
	public String addMessage(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult,
			Model model, @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {

		message.setAuthor(user);

		if (bindingResult.hasErrors()) {
			Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errorsMap);
			model.addAttribute("message", message);
		} else {
//			if (file != null && !file.getOriginalFilename().isEmpty()) {
//				File uploadDirectory = new File(uploadPath);
//				if (!uploadDirectory.exists()) {
//					uploadDirectory.mkdir();
//				}
//				String uuidFile = UUID.randomUUID().toString();
//				String resultFilename = uuidFile + "." + file.getOriginalFilename();
//				file.transferTo(new File(uploadPath + "/" + resultFilename));
//				message.setFilename(resultFilename);
//			}
			saveFile(message, file);
			model.addAttribute("message", null); // if validation was success
			messageRepo.save(message);
		}

		Iterable<Message> messages = messageRepo.findAll();
		model.addAttribute("messages", messages);
		return "main";
	}

	@GetMapping("/user-messages/{user}")
	public String userMessages(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model,
			@RequestParam(required = false) Message message) { // @PathVariable(name = "user...")

		Set<Message> messages = user.getMessages();

		model.addAttribute("userChannel", user);
		model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
		model.addAttribute("subscribersCount", user.getSubscribers().size());
		model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
		model.addAttribute("messages", messages);
		model.addAttribute("message", message);
		model.addAttribute("isCurrentUser", currentUser.equals(user));

		return "userMessages";
	}

	@PostMapping("/user-messages/{user}")
	public String updateMessage(@AuthenticationPrincipal User currentUser, @PathVariable User user,
			@RequestParam("id") Message message, @RequestParam("text") String text, @RequestParam("tag") String tag,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {

		if (message.getAuthor().equals(currentUser)) {
			if (!StringUtils.isEmpty(text)) {
				message.setText(text);
			}
			if (!StringUtils.isEmpty(tag)) {
				message.setTag(tag);
			}
			saveFile(message, file);
			messageRepo.save(message);
		} else {
			// error message
		}
		return "redirect:/user-messages/" + user.getId();
	}

	private void saveFile(Message message, MultipartFile file) throws IllegalStateException, IOException {
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
