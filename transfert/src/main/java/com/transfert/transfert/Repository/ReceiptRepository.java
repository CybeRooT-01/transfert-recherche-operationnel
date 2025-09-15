package com.transfert.transfert.Repository;

import com.transfert.transfert.Entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptNumber(String receiptNumber);

    Optional<Receipt> findByTransaction_TransactionNumber(String transactionNumber);

    @Query("SELECT r FROM Receipt r " +
            "JOIN r.transaction t " +
            "JOIN t.senderAccount sa " +
            "JOIN sa.user su " +
            "WHERE su.id = :userId " +
            "OR t.receiverAccount.user.id = :userId")
    List<Receipt> findAllByUserId(@Param("userId") Long userId);
}
