package com.bank.service;

import com.bank.entity.User;
import com.bank.entity.VirtualCard;
import com.bank.repository.VirtualCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VirtualCardService {

    @Autowired
    private VirtualCardRepository repo;

    // ================= CREATE CARD =================
    public VirtualCard createCardIfNotExists(User user) {

        VirtualCard existing = repo.findByUser(user);

        if (existing != null) {
            return existing;
        }

        VirtualCard card = new VirtualCard();
        card.setUser(user);
        card.setCardNumber(generateCardNumber());
        card.setCvv(String.valueOf(generateCVV()));
        card.setExpiryDate("12/30");
        card.setFrozen(false);

        return repo.save(card);
    }

    // ================= GET CARD =================
    public VirtualCard getCard(User user) {
        return repo.findByUser(user);
    }

    // ================= TOGGLE FREEZE =================
    public void toggleFreeze(User user) {

        VirtualCard card = repo.findByUser(user);

        if (card != null) {
            card.setFrozen(!card.isFrozen());
            repo.save(card);
        }
    }

    // ================= PAYMENT LOGIC =================
    public String pay(User user, Double amount) {

        VirtualCard card = repo.findByUser(user);

        if (card == null) return "Card not found";

        if (card.isFrozen()) return "Card is frozen";

        return "Payment of " + amount + " successful";
    }

    // ================= CARD NUMBER =================
    private String generateCardNumber() {

        Random r = new Random();

        return "4" +
                (100000000000000L + (long)(r.nextDouble() * 900000000000000L));
    }

    // ================= CVV =================
    private int generateCVV() {
        return 100 + new Random().nextInt(900);
    }
}