package com.bank.service;

import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    public void saveTransaction(String type, Double amount, Long accNo) {

        Transaction t = new Transaction();
        t.setType(type);
        t.setAmount(amount);
        t.setAccountNumber(accNo);
        t.setDate(LocalDateTime.now());

        repo.save(t);
    }
}