package com.transfert.transfert.Entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Table(name = "receipts")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE receipts SET deleted_at = NOW() WHERE id = ?")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(name = "receipt_number", length = 50, unique = true, nullable = false)
    private String receiptNumber;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "email_sent")
    private boolean emailSent = false;

    @Column(name = "sms_sent")
    private boolean smsSent = false;

}
