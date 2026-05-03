package com.bank.service;

import com.bank.entity.User;
import com.bank.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public void generateUpiForAllUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getUpiId() == null) {
                user.setUpiId(generateUpi(user.getName()));
                userRepository.save(user);
            }
        }
    }

    // ✅ UPI GENERATOR METHOD
    private String generateUpi(String name) {
        return name.toLowerCase().replaceAll(" ", "")
                + System.currentTimeMillis() + "@bank";
    }

    // SAVE USER (REGISTER)
    public User register(User user) {

        // ✅ AUTO UPI GENERATE
        user.setUpiId(generateUpi(user.getName()));

        return userRepository.save(user);
    }

    // LOGIN
    public User login(String email) {
        return userRepository.findByEmail(email);
    }

    // CHECK EMAIL EXISTS
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
