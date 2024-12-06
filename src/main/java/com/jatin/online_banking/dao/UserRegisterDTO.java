package com.jatin.online_banking.dao;

import lombok.Data;

/**
 * UserRegisterDao.java
 */
@Data
public class UserRegisterDTO {

	private String name;
	private String email;
	private String password;
	private String address;
	private String phoneNumber;
}
