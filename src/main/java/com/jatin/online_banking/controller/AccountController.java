package com.jatin.online_banking.controller;

import com.jatin.online_banking.dao.CreateAccountDTO;
import com.jatin.online_banking.model.Account;
import com.jatin.online_banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDTO createAccountDao) {
        Account newAccount = accountService.createAccount(createAccountDao.getUserId(),
                createAccountDao.getAccountType());
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAllUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAllUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{accountId}/update-balance")
    public ResponseEntity<Account> updateAccountBalance(
            @PathVariable Long accountId,
            @RequestParam BigDecimal newBalance) {
        Account updatedAccount = accountService.updateAccount(accountId, newBalance);
        return ResponseEntity.ok(updatedAccount);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
