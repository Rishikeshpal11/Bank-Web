package com.bank.controller;

import com.bank.entity.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.ContactRepository;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.service.LoanService;
import com.bank.service.UserService;

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
    private ContactRepository contactRepo;

    // ================= CONTACTS =================
    @GetMapping("/contacts")
    public String contactsPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("contacts", contactRepo.findAll());

        return "admin-contacts";
    }

    // ================= FIX UPI =================
    @GetMapping("/fix-upi")
    @ResponseBody
    public String fixUpi() {

        userService.generateUpiForAllUsers();

        return "UPI updated successfully";
    }

    // ================= LOANS PAGE =================
    @GetMapping("/loans")
    public String allLoans(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("loans", loanService.getAllLoans());

        return "admin-loan";
    }

    // ================= APPROVE LOAN =================
    @GetMapping("/approve-loan/{id}")
    public String approve(@PathVariable Long id) {

        loanService.approveLoan(id);

        return "redirect:/admin/loans";
    }

    // ================= REJECT LOAN =================
    @GetMapping("/reject-loan/{id}")
    public String reject(@PathVariable Long id) {

        loanService.rejectLoan(id);

        return "redirect:/admin/loans";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("user");

        // DEBUG
        System.out.println("SESSION USER = " + user);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "admin-dashboard";
    }

    // ================= USERS API =================
    @ResponseBody
    @GetMapping("/users")
    public Object getUsers() {

        return userRepo.findAll();
    }

    // ================= USERS PAGE =================
    @GetMapping("/users-page")
    public String usersPage(HttpSession session,
                            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("users", userRepo.findAll());

        return "users";
    }

    // ================= DELETE USER =================
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {

        userRepo.deleteById(id);

        return "redirect:/admin/users-page";
    }

    // ================= TRANSACTIONS PAGE =================
    @GetMapping("/transactions-page")
    public String txnPage(HttpSession session,
                          Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("txns", txnRepo.findAll());

        return "transactions";
    }

    // ================= TRANSACTIONS API =================
    @ResponseBody
    @GetMapping("/api/transactions")
    public Object getTransactions() {

        return txnRepo.findAll();
    }

    // ================= ACCOUNTS PAGE =================
    @GetMapping("/accounts")
    public String accountsPage(HttpSession session,
                               Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("accounts", accountRepo.findAll());

        return "accounts";
    }
}