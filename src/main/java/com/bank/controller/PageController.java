package com.bank.controller;

import com.bank.entity.Account;
import com.bank.entity.User;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import com.bank.service.VirtualCardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private VirtualCardService virtualCardService;

    // ---------------- HOME ----------------
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ---------------- LOGIN PAGE ----------------
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ---------------- LOGIN PROCESS ----------------
    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session) {

        User dbUser = userService.login(user.getEmail());

        if (dbUser != null 
            && dbUser.getPassword().equals(user.getPassword())
            && dbUser.getRole().equalsIgnoreCase(user.getRole())) {

            session.setAttribute("user", dbUser);

            // optional: card create
            virtualCardService.createCardIfNotExists(dbUser);

            if ("ADMIN".equalsIgnoreCase(dbUser.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/dashboard";
            }
        }

        return "redirect:/login?error=invalid";
    }

    // ---------------- DASHBOARD ----------------
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Account acc = accountService.getByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("account", acc);

        return "dashboard";
    }

    // ---------------- REGISTER PAGE ----------------
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ---------------- REGISTER PROCESS (FIXED) ----------------
    @PostMapping("/register")
    public String register(User user) {

        try {

            // default role
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }

            // 1. SAVE USER
            User savedUser = userService.register(user);

            // 2. CREATE ACCOUNT
            accountService.createAccountIfNotExists(savedUser);

            // 3. AUTO CARD CREATE
            virtualCardService.createCardIfNotExists(savedUser);

            return "redirect:/login?success";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register?error=server";
        }
    }

    // ---------------- TRANSFER ----------------
    @GetMapping("/transfer")
    public String transferPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("account", accountService.getByUser(user));
        return "transfer";
    }

    // ---------------- DEPOSIT ----------------
    @GetMapping("/deposit")
    public String depositPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("account", accountService.getByUser(user));
        return "deposit";
    }

    // ---------------- WITHDRAW ----------------
    @GetMapping("/withdraw")
    public String withdrawPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("account", accountService.getByUser(user));
        return "withdraw";
    }
}