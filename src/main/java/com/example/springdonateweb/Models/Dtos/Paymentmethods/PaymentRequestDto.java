package com.example.springdonateweb.Models.Dtos.Paymentmethods;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private int amount;
    private String note;
    private String userEmail; 
    private int programId;
}
