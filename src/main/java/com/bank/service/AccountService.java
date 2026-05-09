package com.bank.service;

import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repo;

    // ================= GET ACCOUNT BY ID =================

    public Account getAccount(Long id) {

        return repo.findById(id).orElse(null);
    }

    // ================= SAVE ACCOUNT =================

    public void save(Account account) {

        repo.save(account);
    }

    // ================= FIND BY ACCOUNT NUMBER =================

    public Account getByAccountNumber(String accNo) {

        return repo.findByAccountNumber(accNo)
                .orElse(null);
    }

    // ================= DEPOSIT =================

    public void deposit(String accNo, Double amount) {

        Account acc = repo.findByAccountNumber(accNo)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        acc.setBalance(acc.getBalance() + amount);

        repo.save(acc);
    }

    // ================= WITHDRAW =================

    public void withdraw(String accNo, Double amount) {

        Account acc = repo.findByAccountNumber(accNo)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (acc.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);

        repo.save(acc);
    }

    // ================= GET ACCOUNT BY USER =================

    public Account getByUser(User user) {

        if (user == null || user.getId() == null) {
            return null;
        }

        return repo.findByUser_Id(user.getId());
    }

    // ================= CREATE ACCOUNT =================

    public void createAccountIfNotExists(User user) {

        if (user == null || user.getId() == null) {
            throw new RuntimeException("Invalid user");
        }

        Account existing =
                repo.findByUser_Id(user.getId());

        if (existing != null) {
            return;
        }

        Account acc = new Account();

        acc.setUser(user);

        acc.setBalance(0.0);

        // STRING ACCOUNT NUMBER
        acc.setAccountNumber(generateAccountNumber());

        repo.save(acc);
    }

    // ================= GENERATE ACCOUNT NUMBER =================

    private String generateAccountNumber() {

        return "ACC" + System.currentTimeMillis();
    }
}