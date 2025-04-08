package com.example.springdonateweb.Services.interfaces;

import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodCreateDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodResponseDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPaymentMethodService {

    List<PaymentMethodResponseDto> findAll();

    PaymentMethodResponseDto findById(int id);

    PaymentMethodResponseDto create(PaymentMethodCreateDto paymentMethodCreateDto);

    PaymentMethodResponseDto update(int id, PaymentMethodUpdateDto paymentMethodUpdateDto);

    void delete(int id);
    Page<PaymentMethodResponseDto> findPaymentMethodsByPage(int page, int size);
}
