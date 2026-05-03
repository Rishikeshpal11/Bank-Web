package com.bank.service;

import com.bank.entity.Contact;
import com.bank.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repo;

    public void save(Contact c) {
        repo.save(c);
    }
}