package com.melchenko.springboot_mvc.sweater.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.melchenko.springboot_mvc.sweater.domain.Message;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.dto.MessageDTO;
import com.melchenko.springboot_mvc.sweater.reps.MessageRepo;
import com.melchenko.springboot_mvc.sweater.service.MessageService;
import com.melchenko.springboot_mvc.sweater.util.ControllerUtils;

@Controller
public class MessageController {

	private MessageRepo messageRepo;
	
	@Autowired
	private MessageService messageService;

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	public MessageController(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
		return "greeting";
	}

	@GetMapping("/main")
	public String main(@RequestParam(required = false, defaultValue = "") String filter,
			Model model,
			@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@AuthenticationPrincipal User user
	) {
		Page<MessageDTO> page = messageService.messageList(pageable, filter, user);

//		if (filter != null && !filter.isEmpty()) {
//			page = messageRepo.findByTag(filter, pageable);
//		} else {
//			page = messageRepo.findAll(pageable);
//		}
		
		model.addAttribute("page", page);
		model.addAttribute("url", "/main");
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

	@GetMapping("/user-messages/{author}")
	public String userMessages(@AuthenticationPrincipal User currentUser, @PathVariable User author,
			Model model, @RequestParam(required = false) Message message,
			@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable 
	) { 

		Page<MessageDTO> page = messageService.messageListForUser(pageable, currentUser, author);

		model.addAttribute("userChannel", author);
		model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
		model.addAttribute("subscribersCount", author.getSubscribers().size());
		model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
		model.addAttribute("page", page);
		model.addAttribute("message", message);
		model.addAttribute("isCurrentUser", currentUser.equals(author));
		model.addAttribute("url", "/user-messages/" + author.getId());

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
	
	@GetMapping("/messages/{message}/like")
	public String like(
			@AuthenticationPrincipal User currentUser, 
			@PathVariable Message message,
			RedirectAttributes redirectAttributes,
			@RequestHeader(required = false) String referer
	) {
		
		Set<User> likes = message.getLikes();
		
		if (likes.contains(currentUser)) {
			likes.remove(currentUser);
		} else {
			likes.add(currentUser);
		}
		
		UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
		components.getQueryParams().entrySet()
			.forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
		
		return "redirect:" + components.getPath();
	}



}
