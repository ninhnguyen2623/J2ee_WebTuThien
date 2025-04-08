package com.example.springdonateweb.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "programs", schema = "webmomo", catalog = "")
public class ProgramsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "program_id")
    private int programId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "goal_amount")
    private Integer goalAmount;
    @Basic
    @Column(name = "current_amount")
    private Integer currentAmount = 0;

    @Basic
    @Column(name = "image")
    private String image;
    @Basic
    @Column(name = "donation_count")
    private Integer donationCount = 0;
    @Basic
    @Column(name = "start_date")
    private Date startDate;
    @Basic
    @Column(name = "end_date")
    private Date endDate;
    @Basic
    @Column(name = "status")
    private boolean status = true;


    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private CategoriesEntity category;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramsEntity that = (ProgramsEntity) o;
        return programId == that.programId && status == that.status && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(goalAmount, that.goalAmount) && Objects.equals(currentAmount, that.currentAmount) && Objects.equals(donationCount, that.donationCount) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programId, name, description, goalAmount, currentAmount, donationCount, startDate, endDate, status);
    }
}
