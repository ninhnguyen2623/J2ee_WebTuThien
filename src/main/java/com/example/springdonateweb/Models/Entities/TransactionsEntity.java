package com.example.springdonateweb.Models.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "transactions", schema = "webmomo", catalog = "")
public class TransactionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transaction_id")
    private int transactionId;
    @Basic
    @Column(name = "donation_id")
    private Integer donationId;
    @Basic
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic
    @Column(name = "payment_method_id")
    private Integer paymentMethodId;
    @Basic
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    @Basic
    @Column(name = "status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionsEntity that = (TransactionsEntity) o;
        return transactionId == that.transactionId && Objects.equals(donationId, that.donationId) && Objects.equals(amount, that.amount) && Objects.equals(paymentMethodId, that.paymentMethodId) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, donationId, amount, paymentMethodId, transactionDate, status);
    }
}
