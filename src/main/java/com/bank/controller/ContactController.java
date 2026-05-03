package com.bank.controller;

import com.bank.entity.Contact;
import com.bank.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {

    @Autowired
    private ContactRepository contactRepo;

    @PostMapping("/admin/contact/save")
    public String save(@ModelAttribute Contact contact) {
        contactRepo.save(contact);
        return "redirect:/dashboard";
    }
}