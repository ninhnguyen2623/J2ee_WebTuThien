package com.example.springdonateweb.Services.mappers;

import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodCreateDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodResponseDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodUpdateDto;
import com.example.springdonateweb.Models.Entities.PaymentmethodsEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentmethodsEntity toEntity(PaymentMethodCreateDto paymentMethodCreateDto);

    PaymentmethodsEntity toEntity(PaymentMethodUpdateDto paymentMethodUpdateDto);

    PaymentMethodResponseDto toDto(PaymentmethodsEntity paymentmethodsEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentmethodsEntity partialUpdate(PaymentMethodUpdateDto paymentMethodUpdateDto, @MappingTarget PaymentmethodsEntity paymentmethodsEntity);
}
