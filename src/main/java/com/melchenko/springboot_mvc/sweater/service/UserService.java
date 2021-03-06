package com.melchenko.springboot_mvc.sweater.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.melchenko.springboot_mvc.sweater.domain.Role;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.reps.UserRepo;

@Service
public class UserService implements UserDetailsService {

	private final UserRepo userRepo;

	private final MailSender mailSender;
	
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepo userRepo, MailSender mailSender, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.mailSender = mailSender;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		return user;
	}

	public boolean addUser(User user) {
		User userFromDB = userRepo.findByUsername(user.getUsername());

		// if user already exists
		if (userFromDB != null) {
			return false;
		}

		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		user.setActivationCode(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userRepo.save(user);

		sendActivationMail(user);

		return true;
	}

	public boolean activateUser(String code) {
		User user = userRepo.findByActivationCode(code);

		if (user == null) {
			return false;
		}

		user.setActivationCode(null);
		userRepo.save(user);

		return true;
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}

	public void saveUser(User user, String userName, Map<String, String> form) {
		user.setUsername(userName);
		user.getRoles().clear();

		Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
		for (String key : form.keySet()) {
			if (roles.contains(key)) {
				user.getRoles().add(Role.valueOf(key));
			}
		}

		userRepo.save(user);
	}

	public void updateUserProfile(User user, String password, String email) {
		String currentUserEmail = user.getEmail();

		boolean isEmailChanged = ((email != null && !email.equals(currentUserEmail))
				|| (currentUserEmail != null && !currentUserEmail.equals(email)));
		if (isEmailChanged) {
			user.setEmail(email);
			if (!StringUtils.isEmpty(email)) {
				user.setActivationCode(UUID.randomUUID().toString());
			}
		}

		if (!StringUtils.isEmpty(password)) {
			user.setPassword(password);
		}

		userRepo.save(user);

		if (isEmailChanged) {
			sendActivationMail(user);
		}

	}

	private void sendActivationMail(User user) {
		// if user has email
		if (!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello, %s! \n" + "Welcome to Sweater. Please, visit next link - http://localhost:8080/activate/%s",
					user.getUsername(), user.getActivationCode());
			mailSender.sendMail(user.getEmail(), "Activation code", message);
		}

	}

	public void subscribe(User currentUser, User user) {
		user.getSubscribers().add(currentUser);
		userRepo.save(user);
	}

	public void unsubscribe(User currentUser, User user) {
		user.getSubscribers().remove(currentUser);
		userRepo.save(user);
	}

}
