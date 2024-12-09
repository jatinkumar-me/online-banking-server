package com.jatin.online_banking.service;

import com.jatin.online_banking.exception.InsufficientBalanceException;
import com.jatin.online_banking.exception.InvalidTransactionException;
import com.jatin.online_banking.exception.ResourceNotFoundException;
import com.jatin.online_banking.exception.UnauthorizedAccessException;
import com.jatin.online_banking.model.Account;
import com.jatin.online_banking.model.Transaction;
import com.jatin.online_banking.model.TransactionType;
import com.jatin.online_banking.model.UserPrincipal;
import com.jatin.online_banking.repository.AccountRepository;
import com.jatin.online_banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Perform a transaction between two accounts.
     * 
     * @param fromAccountId
     * @param toAccountId
     * @param amount
     * @param description
     */
    public Transaction performTransaction(Account fromAccount, Account toAccount, BigDecimal amount, String description) {
        UserPrincipal currentUser = JWTService.getCurrentUserPrincipal();
        if (!fromAccount.getUserId().equals(currentUser.getUserId())) {
            throw new UnauthorizedAccessException("You are not authorized to perform this transaction");
        }


        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transaction");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transaction amount must be greater than zero");
        }

        // Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(description);
        transaction.setTransactionType(TransactionType.TRANSFER);

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return transactionRepository.save(transaction);
    }

    public Transaction performTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

        return performTransaction(fromAccount, toAccount, amount, description);

    }

    public Transaction performTransaction(Long fromAccountId, String toAccountNumber, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

        return performTransaction(fromAccount, toAccount, amount, description);
    }

    /**
     * Get transactions by account ID.
     * 
     * @param accountId
     */
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        List<Transaction> fromTransactions = transactionRepository.findByFromAccount_AccountId(accountId);
        List<Transaction> toTransactions = transactionRepository.findByToAccount_AccountId(accountId);
        fromTransactions.addAll(toTransactions);
        return fromTransactions;
    }

    /**
     * Get all transactions.
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
