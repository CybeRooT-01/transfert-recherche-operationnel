package com.transfert.transfert.Repository;

import com.transfert.transfert.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
