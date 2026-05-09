package com.bank.controller;

import com.bank.entity.User;
import com.bank.service.LoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService service;

    // USER PAGE
    @GetMapping
    public String loanPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        model.addAttribute("loans", service.getUserLoans(user));

        return "loan";
    }

    // APPLY
    @PostMapping("/apply")
    public String apply(@RequestParam Double amount, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        service.applyLoan(user, amount);

        return "redirect:/loan";
    }

    // ADMIN PAGE
    @GetMapping("/admin")
    public String adminLoans(Model model) {

        model.addAttribute("loans", service.getAllLoans());

        return "admin-loan";
    }

    // APPROVE
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        service.approveLoan(id);
        return "redirect:/loan/admin";
    }

    // REJECT
    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        service.rejectLoan(id);
        return "redirect:/loan/admin";
    }
}