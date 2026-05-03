package com.bank.controller;

import com.bank.repository.*;
import com.bank.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository txnRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;
    @Autowired
    private ContactRepository contactRepo; // 👈 HERE ADD THIS
    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        model.addAttribute("contacts", contactRepo.findAll());
        return "admin-contacts";
    }

    // FIX ALL USERS UPI
    @GetMapping("/fix-upi")
    @ResponseBody
    public String fixUpi() {
        userService.generateUpiForAllUsers();
        return "UPI updated successfully";
    }

    // LOANS
    @GetMapping("/loans")
    public String allLoans(Model model) {
        model.addAttribute("loans", loanService.getAllLoans());
        return "admin-loan";
    }

    @GetMapping("/approve-loan/{id}")
    public String approve(@PathVariable Long id) {
        loanService.approveLoan(id);
        return "redirect:/admin/loans";
    }

    @GetMapping("/reject-loan/{id}")
    public String reject(@PathVariable Long id) {
        loanService.rejectLoan(id);
        return "redirect:/admin/loans";
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "admin-dashboard";
    }

    // USERS
    @ResponseBody
    @GetMapping("/users")
    public Object getUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/users-page")
    public String usersPage(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "users";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return "redirect:/admin/users-page";
    }

    // TRANSACTIONS
    @GetMapping("/transactions-page")
    public String txnPage(Model model) {
        model.addAttribute("txns", txnRepo.findAll());
        return "transactions";
    }

    @ResponseBody
    @GetMapping("/api/transactions")
    public Object getTransactions() {
        return txnRepo.findAll();
    }

    // ACCOUNTS
    @GetMapping("/accounts")
    public String accountsPage(Model model) {
        model.addAttribute("accounts", accountRepo.findAll());
        return "accounts";
    }
    
}
