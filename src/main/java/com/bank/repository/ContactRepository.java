package com.bank.repository;

import com.bank.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // यहाँ कुछ नहीं लिखना जरूरी है
}