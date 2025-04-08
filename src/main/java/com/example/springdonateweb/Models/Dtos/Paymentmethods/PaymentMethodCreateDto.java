package com.example.springdonateweb.Models.Dtos.Paymentmethods;

import lombok.Data;

@Data
public class PaymentMethodCreateDto {
    private String methodName;
    private String description;
    private Byte isActive;
}