package com.bank.repository;

import com.bank.entity.Account;
import com.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // ✅ OPTION 1 (BEST)
    Account findByUser(User user);

    // ✅ OPTION 2 (अगर id से चाहिए)
    Account findByUser_Id(Long id);
}