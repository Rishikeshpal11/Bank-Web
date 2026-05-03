package com.bank.controller;

import com.bank.entity.User;
import com.bank.entity.VirtualCard;
import com.bank.service.VirtualCardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/card")
public class VirtualCardController {

    @Autowired
    private VirtualCardService service;

    // 👉 VIEW CARD
    @GetMapping
    public String viewCard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        VirtualCard card = service.getCard(user);

        // ✅ FIX: null safety
        if (card == null) {
            card = service.createCardIfNotExists(user);
        }

        model.addAttribute("card", card);

        return "card";
    }

    // 👉 CREATE CARD (OPTIONAL)
    @GetMapping("/create")
    public String createCard(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user != null) {
            service.createCardIfNotExists(user);
        }

        return "redirect:/card";
    }

    // 👉 FREEZE / UNFREEZE
    @GetMapping("/toggle")
    public String toggle(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user != null) {
            service.toggleFreeze(user);
        }

        return "redirect:/card";
    }

    // 👉 PAYMENT DEMO
    @PostMapping("/pay")
    public String pay(@RequestParam Double amount, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user != null) {
            String msg = service.pay(user, amount);
            model.addAttribute("msg", msg);
        }

        return "redirect:/card";
    }
}