package com.bank.service;

import com.bank.entity.Account;
import com.bank.entity.Loan;
import com.bank.entity.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepo;
    public List<Loan> getUserLoans(User user) {

        if (user == null) {
            return new ArrayList<>();
        }

        return loanRepo.findByUser(user);
    }

    @Autowired
    private AccountRepository accountRepo;

    // 👉 GET ALL
    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }

    // 👉 APPROVE LOAN (MAIN FIX 🔥)
    public void approveLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId).orElse(null);

        if (loan == null) return;

        // ❌ already approved? stop
        if ("APPROVED".equalsIgnoreCase(loan.getStatus())) {
            return;
        }

        // ✅ 1. Update status
        loan.setStatus("APPROVED");

        // ✅ 2. Get account
        Account account = accountRepo.findByUser(loan.getUser());

        if (account != null) {

            double oldBalance = account.getBalance();
            double loanAmount = loan.getAmount();

            // ✅ 3. Add money
            account.setBalance(oldBalance + loanAmount);

            accountRepo.save(account);
        }

        // ✅ 4. Save loan
        loanRepo.save(loan);
    }

    // 👉 REJECT
    public void rejectLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId).orElse(null);

        if (loan != null) {
            loan.setStatus("REJECTED");
            loanRepo.save(loan);
        }
    }
    public void applyLoan(User user, Double amount) {

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        Loan loan = new Loan();

        loan.setUser(user);
        loan.setAmount(amount);
        loan.setStatus("PENDING"); // ✅ important

        loanRepo.save(loan);
    }
}