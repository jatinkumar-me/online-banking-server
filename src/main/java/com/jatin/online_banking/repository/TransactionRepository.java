package com.jatin.online_banking.repository;

import com.jatin.online_banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByFromAccount_AccountId(Long accountId);

	List<Transaction> findByToAccount_AccountId(Long accountId);
}
