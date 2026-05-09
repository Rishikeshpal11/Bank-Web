package com.bank.repository;

import com.bank.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Account findByUser_Id(Long userId);

    Optional<Account> findByAccountNumber(String accountNumber);
}