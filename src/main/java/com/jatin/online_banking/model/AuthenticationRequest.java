package com.jatin.online_banking.model;

import lombok.Data;

/**
 * AuthenticationRequest
 */
@Data
public class AuthenticationRequest {

	private String email;
	private String password;
}
