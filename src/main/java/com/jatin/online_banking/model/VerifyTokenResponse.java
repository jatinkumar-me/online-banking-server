package com.jatin.online_banking.model;

import lombok.Data;

/**
 * VerifyTokenResponse
 */
@Data
public class VerifyTokenResponse {

	private Long userId;
	private String token;
}
