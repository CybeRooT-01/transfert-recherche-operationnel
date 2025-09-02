package com.transfert.transfert.Repository;

import com.transfert.transfert.Entities.Account;
import com.transfert.transfert.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUser(Optional<Users> user);

    Account findFirstByIsCompanyAccountTrue();
}
