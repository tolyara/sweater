package com.melchenko.springboot_mvc.sweater.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	USER, ADMIN;

	@Override
	public String getAuthority() {		
		return this.name();
	}
}
