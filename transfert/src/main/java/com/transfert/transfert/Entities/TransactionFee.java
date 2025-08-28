package com.transfert.transfert.Entities;


import com.transfert.transfert.Enums.FeeType;
import com.transfert.transfert.Enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_fees")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE transaction_fees SET deleted_at = NOW() WHERE id = ?")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class TransactionFee extends BaseModel {


    @Column(name = "min_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal maxAmount;

    @Enumerated(EnumType.STRING)
    private FeeType feeType;

    @Column(name = "fee_value", precision = 10, scale = 4, nullable = false)
    private BigDecimal feeValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(name = "is_active")
    private boolean isActive = true;

}
