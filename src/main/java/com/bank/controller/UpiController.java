package com.bank.controller;

import com.bank.entity.User;
import com.bank.service.UpiService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/upi")
public class UpiController {

    @Autowired
    private UpiService service;

    @GetMapping
    public String page() {
        return "upi";
    }

    @PostMapping("/pay")
    public String pay(@RequestParam String upi,
                      @RequestParam Double amount,
                      HttpSession session,
                      Model model) {

        User user = (User) session.getAttribute("user");

        try {
            service.transfer(user, upi, amount);

            model.addAttribute("msg", "✅ Payment Successful");

        } catch (Exception e) {
            model.addAttribute("msg", "❌ " + e.getMessage());
        }

        return "upi";
    }
}