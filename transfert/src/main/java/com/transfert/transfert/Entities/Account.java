package com.transfert.transfert.Entities;


import com.transfert.transfert.Enums.AccountStatus;
import com.transfert.transfert.Enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE accounts SET deleted_at = NOW() WHERE id = ?")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseModel{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @Column(name = "account_number", length = 255, unique = true, nullable = false)
    private String accountNumber;

    @Column(name ="failed_attempts")
    private Integer failedAttempts = 0;

    @Column(name="is_company_account")
    private Boolean isCompanyAccount = false;


    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(length = 3)
    private String currency = "XOF";

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "daily_limit", precision = 15, scale = 2)
    private BigDecimal dailyLimit = new BigDecimal("1000000.00");

    @Column(name = "monthly_limit", precision = 15, scale = 2)
    private BigDecimal monthlyLimit = new BigDecimal("5000000.00");

    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType = SubscriptionType.FREE;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Transaction> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Transaction> receivedTransactions = new ArrayList<>();
}
