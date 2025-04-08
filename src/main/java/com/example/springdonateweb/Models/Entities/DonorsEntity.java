package com.example.springdonateweb.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donors", schema = "webmomo", catalog = "")
public class DonorsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "donor_id")
    private int donorId;
    @Basic
    @Column(name = "program_id")
    private Integer programId;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "donation_id")
    private Integer donationId;
    @Basic
    @Column(name = "donation_date")
    private LocalDateTime donationDate;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonorsEntity that = (DonorsEntity) o;
        return donorId == that.donorId && Objects.equals(programId, that.programId) && Objects.equals(userId, that.userId) && Objects.equals(donationId, that.donationId) && Objects.equals(donationDate, that.donationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(donorId, programId, userId, donationId, donationDate);
    }
}
