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
    public String deposit(@RequestParam Long accNo, @RequestParam Double amount) {

        Account acc = accountService.getAccount(accNo);

        if (acc == null) {
            return "redirect:/dashboard?error=AccountNotFound";
        }

        acc.setBalance(acc.getBalance() + amount);
        accountService.save(acc);

        transactionService.saveTransaction("DEPOSIT", amount, accNo);

        return "redirect:/dashboard?success=deposit";
    }

    // ================== WITHDRAW ==================
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long accNo, @RequestParam Double amount) {

        Account acc = accountService.getAccount(accNo);

        // ❌ account नहीं मिला
        if (acc == null) {
            return "redirect:/dashboard?error=AccountNotFound";
        }

        // ❌ insufficient balance
        if (acc.getBalance() < amount) {
            return "redirect:/dashboard?error=InsufficientBalance";
        }

        // ✅ withdraw
        acc.setBalance(acc.getBalance() - amount);
        accountService.save(acc);

        transactionService.saveTransaction("WITHDRAW", amount, accNo);

        return "redirect:/dashboard?success=withdraw";
    }

    // ================== TRANSFER ==================
    @PostMapping("/transfer")
    public String transfer(
            @RequestParam Long fromAccount,
            @RequestParam Long toAccount,
            @RequestParam Double amount) {

        Account from = accountService.getAccount(fromAccount);
        Account to = accountService.getAccount(toAccount);

        // ❌ account not found
        if (from == null || to == null) {
            return "redirect:/transfer?error=AccountNotFound";
        }

        // ❌ insufficient balance
        if (from.getBalance() < amount) {
            return "redirect:/transfer?error=InsufficientBalance";
        }

        // ✅ transfer
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountService.save(from);
        accountService.save(to);

        transactionService.saveTransaction("TRANSFER-OUT", amount, from.getAccountNumber());
        transactionService.saveTransaction("TRANSFER-IN", amount, to.getAccountNumber());

        // ✅ SUCCESS → dashboard
        return "redirect:/dashboard?success=transfer";
    }
}