package com.transfert.transfert.Repository;

import com.transfert.transfert.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t " +
            "WHERE (t.senderAccount.id = :accountId OR t.receiverAccount.id = :accountId) " +
            "AND t.createdAt BETWEEN :startDate AND :endDate " +
            "AND t.status = com.transfert.transfert.Enums.TransactionStatus.COMPLETED")
    Optional<BigDecimal> sumTransactionsByAccountAndDateRange(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
