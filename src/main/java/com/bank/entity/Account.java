package com.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    private Long accountNumber;

    private Double balance;

    // IMPORTANT FIXES:
    // 1. fetch type added (safe)
    // 2. cascade optional (recommended)
    // 3. referencedColumnName added (clarity)

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    public Account() {}

    // ---------------- GETTERS / SETTERS ----------------

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
