package com.example.springdonateweb.Models.Dtos.Paymentmethods;
import lombok.Data;

@Data
public class PaymentMethodUpdateDto {
    private String methodName;
    private String description;
    private Byte isActive;
}