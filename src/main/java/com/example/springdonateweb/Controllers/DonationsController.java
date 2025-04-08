package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Donations.DonationCreateDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationUpdateDto;
import com.example.springdonateweb.Services.interfaces.IDonationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/donations")
public class DonationsController {

    private final IDonationsService donationsService;

    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DonationResponseDto> donationPage = donationsService.findDonationsByPage(page, size);
        model.addAttribute("donations", donationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", donationPage.getTotalPages());
        return "admin/Donations/index";
    }
    @GetMapping("/top-donors")
    @ResponseBody
    public List<Map<String, Object>> getTopDonors(@RequestParam(defaultValue = "5") int limit) {
        return donationsService.getTopDonors(limit);
    }

    @GetMapping("/total-donations-by-day")
    @ResponseBody
    public Map<String, BigDecimal> getTotalDonationsByDay() {
        return donationsService.getTotalDonationsByDay();
    }
    @GetMapping("/total-donations-by-program")
    @ResponseBody
    public Map<Integer, BigDecimal> getTotalDonationsByProgram() {
        return donationsService.getTotalDonationsByProgram();
    }
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("donation", new DonationCreateDto());
        return "admin/Donations/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute DonationCreateDto donationCreateDto) {
        donationsService.create(donationCreateDto);
        return "redirect:/admin/donations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        DonationResponseDto donation = donationsService.findById(id);
        if (donation == null) return "redirect:/admin/donations";
        model.addAttribute("donation", donation);
        return "admin/Donations/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute DonationUpdateDto donationUpdateDto) {
        donationsService.update(id, donationUpdateDto);
        return "redirect:/admin/donations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        donationsService.delete(id);
        return "redirect:/admin/donations";
    }
}
