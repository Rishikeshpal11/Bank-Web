package com.bank.service;

import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    // SAVE TRANSACTION
    public void saveTransaction(String type,
                                Double amount,
                                String accountNumber) {

        Transaction tx = new Transaction();

        tx.setType(type);
        tx.setAmount(amount);

        // ACCOUNT NUMBER STRING
        tx.setAccountNumber(accountNumber);

        repo.save(tx);
    }
}