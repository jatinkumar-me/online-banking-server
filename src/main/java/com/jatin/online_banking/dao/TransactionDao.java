package com.jatin.online_banking.dao;

import java.math.BigDecimal;

import lombok.Data;

/**
 * TransactionDao
 */
@Data
public class TransactionDao {

	Long fromAccountId;
	Long toAccountId;
	BigDecimal amount;
	String description;
}
