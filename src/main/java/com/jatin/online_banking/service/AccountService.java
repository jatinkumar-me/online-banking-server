package com.jatin.online_banking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jatin.online_banking.exception.ResourceNotFoundException;
import com.jatin.online_banking.model.Account;
import com.jatin.online_banking.model.AccountType;
import com.jatin.online_banking.model.User;
import com.jatin.online_banking.repository.AccountRepository;
import com.jatin.online_banking.repository.UserRepository;

/**
 * AccountService
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new account for the user.
     */
    public Account createAccount(Long userId, AccountType accountType) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Account newAccount = new Account();
            newAccount.setUserId(userId);
            newAccount.setBalance(BigDecimal.ZERO);
            newAccount.setAccountNumber(generateAccountNumber());
            newAccount.setAccountType(accountType);
            newAccount.setCreatedAt(LocalDateTime.now());
            return accountRepository.save(newAccount);
        }
        throw new ResourceNotFoundException("User not found");
    }

    /**
     * Get the details of a user's account.
     */
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    /**
     * Update the balance of an account.
     */
    public Account updateAccount(Long accountId, BigDecimal newBalance) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    /**
     * Delete an account.
     */
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        accountRepository.delete(account);
    }

    /**
     * Generate a unique account number.
     */
    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }

    /**
     * Get all accounts of a user.
     */
    public List<Account> getAllUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
