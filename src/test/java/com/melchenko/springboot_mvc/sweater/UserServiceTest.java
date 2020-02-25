package com.melchenko.springboot_mvc.sweater;

import java.util.Collections;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.melchenko.springboot_mvc.sweater.domain.Role;
import com.melchenko.springboot_mvc.sweater.domain.User;
import com.melchenko.springboot_mvc.sweater.reps.UserRepo;
import com.melchenko.springboot_mvc.sweater.service.MailSender;
import com.melchenko.springboot_mvc.sweater.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepo userRepo;
	
	@MockBean
	private MailSender mailSender;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void addUser() {
		User user = new User();
		user.setEmail("someMailForTest@gmail.com");
		boolean isUserCreated = userService.addUser(user);
		
		Assert.assertTrue(isUserCreated);
		Assert.assertNotNull(user.getActivationCode());
		Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
		
		Mockito.verify(userRepo, Mockito.times(1)).save(user);
		Mockito.verify(mailSender, Mockito.times(1)).sendMail(
				ArgumentMatchers.eq(user.getEmail()), 
//				ArgumentMatchers.eq("Activation code"), 
//				ArgumentMatchers.contains("Welcome to Sweater."));	
				ArgumentMatchers.anyString(), 
				ArgumentMatchers.anyString());	
		
	}
	
	//test for fail case
	@Test
	public void addUserIfExists() {
		User user = new User();
		user.setUsername("John");
		Mockito.doReturn(new User()).when(userRepo).findByUsername("John");
		boolean isUserCreated = userService.addUser(user);
		
		Assert.assertFalse(isUserCreated);
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
		Mockito.verify(mailSender, Mockito.times(0)).sendMail(
				ArgumentMatchers.anyString(), 
				ArgumentMatchers.anyString(), 
				ArgumentMatchers.anyString());

	}
	
	@Test
	public void activateUser() {
		User user = new User();
		user.setActivationCode("bingo!");
		Mockito.doReturn(user).when(userRepo).findByActivationCode("Activation code");
		boolean isUserActivated = userService.activateUser("Activation code");
		
		Assert.assertTrue(isUserActivated);
		Assert.assertNull(user.getActivationCode());
		
		Mockito.verify(userRepo, Mockito.times(1)).save(user);
	}
	
	@Test
	public void activateUserFailCase() {
		boolean isUserActivated = userService.activateUser("Activate me");
		
		Assert.assertFalse(isUserActivated);
		
		Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
	}
	
	
	

}
