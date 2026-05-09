package com.bank.controller;

import com.bank.entity.Account;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    // ================== DEPOSIT ==================
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accNo,
                          @RequestParam Double amount) {

        // FIND ACCOUNT BY ACCOUNT NUMBER
        Account acc = accountService.getByAccountNumber(accNo);

        // ACCOUNT NOT FOUND
        if (acc == null) {
            return "redirect:/dashboard?error=AccountNotFound";
        }

        // INVALID AMOUNT
        if (amount == null || amount <= 0) {
            return "redirect:/dashboard?error=InvalidAmount";
        }

        // UPDATE BALANCE
        acc.setBalance(acc.getBalance() + amount);

        // SAVE
        accountService.save(acc);

        // SAVE TRANSACTION
        transactionService.saveTransaction(
                "DEPOSIT",
                amount,
                acc.getAccountNumber()
        );

        return "redirect:/dashboard?success=DepositSuccess";
    }

    // ================== WITHDRAW ==================
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accNo,
                           @RequestParam Double amount) {

        // FIND ACCOUNT
        Account acc = accountService.getByAccountNumber(accNo);

        // ACCOUNT NOT FOUND
        if (acc == null) {
            return "redirect:/dashboard?error=AccountNotFound";
        }

        // INVALID AMOUNT
        if (amount == null || amount <= 0) {
            return "redirect:/dashboard?error=InvalidAmount";
        }

        // INSUFFICIENT BALANCE
        if (acc.getBalance() < amount) {
            return "redirect:/dashboard?error=InsufficientBalance";
        }

        // UPDATE BALANCE
        acc.setBalance(acc.getBalance() - amount);

        // SAVE
        accountService.save(acc);

        // SAVE TRANSACTION
        transactionService.saveTransaction(
                "WITHDRAW",
                amount,
                acc.getAccountNumber()
        );

        return "redirect:/dashboard?success=WithdrawSuccess";
    }

    // ================== TRANSFER ==================
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String fromAcc,
            @RequestParam String toAcc,
            @RequestParam Double amount) {

        // FIND SENDER
        Account from =
                accountService.getByAccountNumber(fromAcc);

        // FIND RECEIVER
        Account to =
                accountService.getByAccountNumber(toAcc);

        // ACCOUNT NOT FOUND
        if (from == null || to == null) {
            return "redirect:/transfer?error=AccountNotFound";
        }

        // INVALID AMOUNT
        if (amount == null || amount <= 0) {
            return "redirect:/transfer?error=InvalidAmount";
        }

        // INSUFFICIENT BALANCE
        if (from.getBalance() < amount) {
            return "redirect:/transfer?error=InsufficientBalance";
        }

        // DEDUCT MONEY
        from.setBalance(from.getBalance() - amount);

        // ADD MONEY
        to.setBalance(to.getBalance() + amount);

        // SAVE BOTH
        accountService.save(from);
        accountService.save(to);

        // SAVE TRANSACTIONS
        transactionService.saveTransaction(
                "TRANSFER-OUT",
                amount,
                from.getAccountNumber()
        );

        transactionService.saveTransaction(
                "TRANSFER-IN",
                amount,
                to.getAccountNumber()
        );

        return "redirect:/dashboard?success=TransferSuccess";
    }
}