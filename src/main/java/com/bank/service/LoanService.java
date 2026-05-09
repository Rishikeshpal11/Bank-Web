package com.bank.service;

import com.bank.entity.Account;
import com.bank.entity.Loan;
import com.bank.entity.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.LoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    // ================= USER LOANS =================

    public List<Loan> getUserLoans(User user) {
        return loanRepository.findByUser(user);
    }

    // ================= ADMIN ALL LOANS =================

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // ================= APPLY LOAN =================

    public void applyLoan(User user, Double amount) {

        Loan loan = new Loan();

        loan.setUser(user);
        loan.setAmount(amount);
        loan.setStatus("PENDING");

        loanRepository.save(loan);
    }

    // ================= APPROVE LOAN =================

    public void approveLoan(Long id) {

        Loan loan = loanRepository.findById(id).orElse(null);

        if (loan != null && loan.getStatus().equals("PENDING")) {

            // UPDATE LOAN STATUS
            loan.setStatus("APPROVED");
            loanRepository.save(loan);

            // GET USER
            User user = loan.getUser();

            // GET USER ACCOUNT
            Account account = user.getAccount();

            if (account != null) {

                // IF BALANCE NULL
                if (account.getBalance() == null) {
                    account.setBalance(0.0);
                }

                // ADD LOAN AMOUNT TO BALANCE
                Double updatedBalance =
                        account.getBalance() + loan.getAmount();

                account.setBalance(updatedBalance);

                // SAVE UPDATED ACCOUNT
                accountRepository.save(account);
            }
        }
    }

    // ================= REJECT LOAN =================

    public void rejectLoan(Long id) {

        Loan loan = loanRepository.findById(id).orElse(null);

        if (loan != null && loan.getStatus().equals("PENDING")) {

            loan.setStatus("REJECTED");

            loanRepository.save(loan);
        }
    }
}