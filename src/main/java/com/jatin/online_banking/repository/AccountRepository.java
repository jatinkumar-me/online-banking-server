package com.jatin.online_banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.online_banking.model.Account;

/**
 * AccountRepository
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(String accountNumber);

	List<Account> findByUserId(Long userId);
}
