package com.jatin.online_banking.dao;

import com.jatin.online_banking.model.AccountType;

import lombok.Data;

/**
 * CreateAccountDao
 */
@Data
public class CreateAccountDTO {

	private Long userId;
	private AccountType accountType;
}
