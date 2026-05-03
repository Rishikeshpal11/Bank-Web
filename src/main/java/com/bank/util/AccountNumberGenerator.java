package com.bank.util;

public class AccountNumberGenerator {

    // Generates unique account number using timestamp
    public static Long generate() {
        return System.currentTimeMillis();
    }
}