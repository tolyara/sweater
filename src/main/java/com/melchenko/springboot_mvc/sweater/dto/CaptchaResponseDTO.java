package com.melchenko.springboot_mvc.sweater.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDTO {
	
	private boolean success;
	
	@JsonAlias("error-codes")
	private Set<String> errorCodes;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Set<String> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(Set<String> errorCodes) {
		this.errorCodes = errorCodes;
	}

}
