//package com.example.springdonateweb.Services;
//
//
//import com.example.springdonateweb.Models.Entities.PaymentmethodsEntity;
//import com.example.springdonateweb.Repositories.PaymentRepository;
//import com.example.springdonateweb.Services.interfaces.IPaymentmethodsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Service
//@RequiredArgsConstructor
//public class PaymentmethodsService implements IPaymentmethodsService {
//    private final PaymentmethodsService paymentmethodsService;
//    private final PaymentmethodsEntity paymentmethodsEntity;
//    private final PaymentRepository paymentMethodsRepository;
//    @Override
//    public PaymentmethodsEntity findByIsActive(Byte isActive) {
//        return paymentMethodsRepository.findAll()
//                .stream()
//                .filter(paymentmethodsEntity -> paymentmethodsEntity.getIsActive().equals(isActive))
//                .findFirst()
//                .orElse(null);
//
//    }
//
//    @Override
//    public PaymentmethodsEntity findByMethodName(String methodName) {
//        return paymentMethodsRepository.findAll()
//                .stream()
//                .filter(paymentmethodsEntity -> paymentmethodsEntity.getMethodName().equals(methodName))
//                .findFirst()
//                .orElse(null);
//
//    }
//
//    @Override
//    public List<PaymentmethodsEntity> findAll() {
//        return paymentMethodsRepository.findAll().stream().collect(Collectors.toList());
//
//
//    }
//}
