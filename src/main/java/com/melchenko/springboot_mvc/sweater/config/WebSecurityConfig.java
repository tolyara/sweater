package com.melchenko.springboot_mvc.sweater.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.melchenko.springboot_mvc.sweater.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	private final DataSource dataSource;

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(8);
	}

//	@Autowired
//	public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
//		this.userService = userService;
//		this.passwordEncoder = passwordEncoder;
////		this.dataSource = dataSource;
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home", "/registration", "/static/**", "/activate/*").permitAll().anyRequest().authenticated()
				.and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
	}

//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user1")
//				.password("password1")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
		
		
		
//		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());

//		authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
//				.passwordEncoder(NoOpPasswordEncoder.getInstance());
//				.usersByUsernameQuery("select username, password, active from usr where username = ?")
//				.authoritiesByUsernameQuery(
//						"select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username = ? ");
	}

}
