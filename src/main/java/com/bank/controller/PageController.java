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

    // ================= HOME =================
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ================= LOGIN PAGE =================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ================= LOGIN PROCESS =================
    @PostMapping("/login")
    public String login(@ModelAttribute User user,
                        HttpSession session) {

        User dbUser = userService.login(user.getEmail());

        if (dbUser != null
                && dbUser.getPassword().equals(user.getPassword())
                && dbUser.getRole().equalsIgnoreCase(user.getRole())) {

            session.setAttribute("user", dbUser);

            if ("ADMIN".equalsIgnoreCase(dbUser.getRole())) {
                return "redirect:/admin/dashboard";
            }

            return "redirect:/dashboard";
        }

        return "redirect:/login?error";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session,
                            Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Account account = accountService.getByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("account", account);

        return "dashboard";
    }

    // ================= REGISTER PAGE =================
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // ================= REGISTER PROCESS =================
    @PostMapping("/register")
    public String register(User user) {

        try {

            if (user.getRole() == null
                    || user.getRole().isEmpty()) {

                user.setRole("USER");
            }

            User savedUser = userService.register(user);

            accountService.createAccountIfNotExists(savedUser);

            virtualCardService.createCardIfNotExists(savedUser);

            return "redirect:/login?success";

        } catch (Exception e) {

            e.printStackTrace();

            return "redirect:/register?error";
        }
    }

    // ================= TRANSFER PAGE =================
    @GetMapping("/transfer")
    public String transferPage(HttpSession session,
                               Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("account",
                accountService.getByUser(user));

        return "transfer";
    }

    // ================= TRANSFER PROCESS =================
    @PostMapping("/transfer")
    public String transferMoney(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam double amount) {

        Account sender =
                accountService.getByAccountNumber(fromAccount);

        Account receiver =
                accountService.getByAccountNumber(toAccount);

        // account not found
        if (sender == null || receiver == null) {
            return "redirect:/transfer?error=AccountNotFound";
        }

        // invalid amount
        if (amount <= 0) {
            return "redirect:/transfer?error=InvalidAmount";
        }

        // insufficient balance
        if (sender.getBalance() < amount) {
            return "redirect:/transfer?error=InsufficientBalance";
        }

        // transfer
        sender.setBalance(sender.getBalance() - amount);

        receiver.setBalance(receiver.getBalance() + amount);

        accountService.save(sender);
        accountService.save(receiver);

        return "redirect:/dashboard?success";
    }

    // ================= DEPOSIT PAGE =================
    @GetMapping("/deposit")
    public String depositPage(HttpSession session,
                              Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("account",
                accountService.getByUser(user));

        return "deposit";
    }

    // ================= DEPOSIT PROCESS =================
    @PostMapping("/deposit")
    public String depositMoney(
            @RequestParam String accNo,
            @RequestParam double amount,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Account account =
                accountService.getByAccountNumber(accNo);

        if (account == null) {
            return "redirect:/deposit?error=AccountNotFound";
        }

        if (amount <= 0) {
            return "redirect:/deposit?error=InvalidAmount";
        }

        account.setBalance(
                account.getBalance() + amount
        );

        accountService.save(account);

        return "redirect:/dashboard?success";
    }

    // ================= WITHDRAW PAGE =================
    @GetMapping("/withdraw")
    public String withdrawPage(HttpSession session,
                               Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("account",
                accountService.getByUser(user));

        return "withdraw";
    }

    // ================= WITHDRAW PROCESS =================
    @PostMapping("/withdraw")
    public String withdrawMoney(
            @RequestParam String accNo,
            @RequestParam double amount,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Account account =
                accountService.getByAccountNumber(accNo);

        if (account == null) {
            return "redirect:/withdraw?error=AccountNotFound";
        }

        if (amount <= 0) {
            return "redirect:/withdraw?error=InvalidAmount";
        }

        if (account.getBalance() < amount) {
            return "redirect:/withdraw?error=InsufficientBalance";
        }

        account.setBalance(
                account.getBalance() - amount
        );

        accountService.save(account);

        return "redirect:/dashboard?success";
    }
}