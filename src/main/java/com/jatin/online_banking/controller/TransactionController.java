package com.jatin.online_banking.controller;

import com.jatin.online_banking.dao.TransactionDao;
import com.jatin.online_banking.model.Transaction;
import com.jatin.online_banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> performTransaction(@RequestBody TransactionDao transactionDao) {
        Transaction transaction = transactionService.performTransaction(
                transactionDao.getFromAccountId(),
                transactionDao.getFromAccountId(),
                transactionDao.getAmount(),
                transactionDao.getDescription());
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
