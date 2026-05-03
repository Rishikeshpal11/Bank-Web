package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
    User findByUpiId(String upiId);
}