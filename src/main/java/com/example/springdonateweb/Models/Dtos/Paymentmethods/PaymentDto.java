package com.example.springdonateweb.Models.Dtos.Paymentmethods;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String amount;
    private String paymentMethod;
}
