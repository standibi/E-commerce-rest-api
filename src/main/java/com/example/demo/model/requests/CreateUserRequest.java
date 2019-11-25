package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateUserRequest {

	@JsonProperty
	private String username;

	@NotNull
	private String password;

	@NotNull
	private String password_confirmation;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword_confirmation() {
		return password_confirmation;
	}

	public void setPassword_confirmation(String password_confirmation) {
		this.password_confirmation = password_confirmation;
	}
}
