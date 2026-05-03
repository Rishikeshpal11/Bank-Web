package com.bank.repository;

import com.bank.entity.User;
import com.bank.entity.VirtualCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualCardRepository extends JpaRepository<VirtualCard, Long> {

    VirtualCard findByUser(User user);
}