package com.jatin.online_banking.exception;

/**
 * InsufficientBalanceException
 */
public class InsufficientBalanceException extends RuntimeException{

	public InsufficientBalanceException(String message) {
		super(message);
	}
	
}
