package com.jatin.online_banking.dao;

import com.jatin.online_banking.model.AccountType;

import lombok.Data;

/**
 * CreateAccountDao
 */
@Data
public class CreateAccountDao {

	private Long userId;
	private AccountType accountType;
}
