package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodCreateDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodResponseDto;
import com.example.springdonateweb.Models.Dtos.Paymentmethods.PaymentMethodUpdateDto;
import com.example.springdonateweb.Services.interfaces.IPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/paymentmethods")
public class PaymentMethodController {
    
    private final IPaymentMethodService paymentMethodService;
    
    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PaymentMethodResponseDto> paymentMethodPage = paymentMethodService.findPaymentMethodsByPage(page, size);
        model.addAttribute("paymentMethods", paymentMethodPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentMethodPage.getTotalPages());
        return "admin/Paymentmethods/index";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        PaymentMethodCreateDto paymentMethodCreateDto = new PaymentMethodCreateDto();
        // Explicitly set isActive to 1 (byte) to ensure it's checked by default
        Byte isActiveValue = 1;
        paymentMethodCreateDto.setIsActive(isActiveValue);
        model.addAttribute("paymentMethod", paymentMethodCreateDto);
        return "admin/Paymentmethods/create";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute PaymentMethodCreateDto paymentMethodCreateDto) {
        // Default to active if not set
        if (paymentMethodCreateDto.getIsActive() == null) {
            paymentMethodCreateDto.setIsActive((byte) 1);
        }
        paymentMethodService.create(paymentMethodCreateDto);
        return "redirect:/admin/paymentmethods";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        PaymentMethodResponseDto paymentMethod = paymentMethodService.findById(id);
        if (paymentMethod == null)
            return "redirect:/admin/paymentmethods";
        model.addAttribute("paymentMethod", paymentMethod);
        return "admin/Paymentmethods/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute PaymentMethodUpdateDto paymentMethodUpdateDto) {
        // Ensure isActive is not null
        if (paymentMethodUpdateDto.getIsActive() == null) {
            paymentMethodUpdateDto.setIsActive((byte) 0);
        }
        paymentMethodService.update(id, paymentMethodUpdateDto);
        return "redirect:/admin/paymentmethods";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        paymentMethodService.delete(id);
        return "redirect:/admin/paymentmethods";
    }
}
