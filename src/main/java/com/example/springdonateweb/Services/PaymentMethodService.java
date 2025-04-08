package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodCreateDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodResponseDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodUpdateDto;
import com.example.springdonateweb.Models.Entities.PaymentmethodsEntity;
import com.example.springdonateweb.Repositories.PaymentMethodRepository;
import com.example.springdonateweb.Services.interfaces.IPaymentMethodService;
import com.example.springdonateweb.Services.mappers.PaymentMethodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService implements IPaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public List<PaymentMethodResponseDto> findAll() {
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodResponseDto findById(int id) {
        return paymentMethodRepository.findById(id)
                .map(paymentMethodMapper::toDto)
                .orElse(null);
    }

    @Override
    public PaymentMethodResponseDto create(PaymentMethodCreateDto paymentMethodCreateDto) {
        PaymentmethodsEntity paymentMethodEntity = paymentMethodMapper.toEntity(paymentMethodCreateDto);
        return paymentMethodMapper.toDto(paymentMethodRepository.save(paymentMethodEntity));
    }
    @Override
    public Page<PaymentMethodResponseDto> findPaymentMethodsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PaymentmethodsEntity> paymentMethodPage = paymentMethodRepository.findAll(pageable);
        return paymentMethodPage.map(paymentMethodMapper::toDto);
    }
    @Override
    public PaymentMethodResponseDto update(int id, PaymentMethodUpdateDto paymentMethodUpdateDto) {
        return paymentMethodRepository.findById(id)
                .map(existingPaymentMethod -> {
                    PaymentmethodsEntity updatedPaymentMethod = paymentMethodMapper.partialUpdate(paymentMethodUpdateDto, existingPaymentMethod);
                    return paymentMethodMapper.toDto(paymentMethodRepository.save(updatedPaymentMethod));
                })
                .orElse(null);
    }

    @Override
    public void delete(int id) {
        paymentMethodRepository.deleteById(id);
    }
}
