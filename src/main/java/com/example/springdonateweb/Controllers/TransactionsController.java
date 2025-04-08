package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Transactions.TransactionCreateDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionResponseDto;
import com.example.springdonateweb.Models.Dtos.Transactions.TransactionUpdateDto;
import com.example.springdonateweb.Services.interfaces.ITransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/transactions")
public class TransactionsController {

    private final ITransactionsService transactionsService;

    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponseDto> transactionsPage = transactionsService.findTransactionsByPage(page, size);
        model.addAttribute("transactions", transactionsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transactionsPage.getTotalPages());
        return "admin/Transactions/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("transaction", new TransactionCreateDto());
        return "admin/Transactions/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TransactionCreateDto transactionCreateDto) {
        transactionsService.create(transactionCreateDto);
        return "redirect:/admin/transactions";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        TransactionResponseDto transaction = transactionsService.findById(id);
        if (transaction == null) return "redirect:/admin/transactions";
        model.addAttribute("transaction", transaction);
        return "admin/Transactions/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute TransactionUpdateDto transactionUpdateDto) {
        transactionsService.update(id, transactionUpdateDto);
        return "redirect:/admin/transactions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        transactionsService.delete(id);
        return "redirect:/admin/transactions";
    }
}
