package com.bank.service;

import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpiService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private TransactionRepository txnRepo;

    // 🔥 MAIN TRANSFER LOGIC
    public void transfer(User sender, String receiverUpi, Double amount) {

        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }

        User receiver = userRepo.findByUpiId(receiverUpi);

        if (receiver == null) {
            throw new RuntimeException("Receiver not found");
        }

        Account senderAcc = accountRepo.findByUser_Id(sender.getId());
        Account receiverAcc = accountRepo.findByUser_Id(receiver.getId());

        if (senderAcc == null || receiverAcc == null) {
            throw new RuntimeException("Account not found");
        }

        if (senderAcc.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💸 DEBIT
        senderAcc.setBalance(senderAcc.getBalance() - amount);

        // 💰 CREDIT
        receiverAcc.setBalance(receiverAcc.getBalance() + amount);

        accountRepo.save(senderAcc);
        accountRepo.save(receiverAcc);

        // 🔥 TRANSACTION SAVE

        Transaction t1 = new Transaction();
        t1.setAccountNumber(senderAcc.getAccountNumber());
        t1.setAmount(amount);
        t1.setType("TRANSFER-OUT");
        t1.setDate(java.time.LocalDateTime.now());
        txnRepo.save(t1);

        Transaction t2 = new Transaction();
        t2.setAccountNumber(receiverAcc.getAccountNumber());
        t2.setAmount(amount);
        t2.setType("TRANSFER-IN");
        t2.setDate(java.time.LocalDateTime.now());
        txnRepo.save(t2);
    }
}