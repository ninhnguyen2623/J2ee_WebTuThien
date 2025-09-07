package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Programs.ProgramsResponseDto;
import com.example.springdonateweb.Services.interfaces.IDonationsService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/donations")
public class DonationsController {
    
    private final IDonationsService donationsService;
    private final IProgramsService programsService;
    
    @GetMapping("")
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        
        // Chuyển đổi chuỗi ngày thành LocalDate (nếu có)
        LocalDate fromDate = null;
        LocalDate toDate = null;
        
        if (dateFrom != null && !dateFrom.isEmpty()) {
            fromDate = LocalDate.parse(dateFrom);
        }
        
        if (dateTo != null && !dateTo.isEmpty()) {
            toDate = LocalDate.parse(dateTo);
        }
        
        // Lấy danh sách quyên góp theo trang và bộ lọc (nếu có)
        Page<DonationResponseDto> donationPage;
        
        if ((searchTerm != null && !searchTerm.isEmpty()) || fromDate != null || toDate != null) {
            // Tìm kiếm với các tiêu chí
            donationPage = donationsService.searchDonations(searchTerm, fromDate, toDate, page, size);
        } else {
            // Lấy tất cả quyên góp theo trang
            donationPage = donationsService.findDonationsByPage(page, size);
        }
        
        // Lấy tất cả chương trình để hiển thị tên thay vì chỉ ID
        List<ProgramsResponseDto> allPrograms = programsService.findAll();
        Map<Integer, String> programNames = allPrograms.stream()
                                                       .collect(Collectors.toMap(ProgramsResponseDto::getProgramId, ProgramsResponseDto::getName));
        
        // Thêm dữ liệu vào model
        model.addAttribute("donations", donationPage.getContent());
        model.addAttribute("programNames", programNames);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", donationPage.getTotalPages());
        model.addAttribute("totalItems", donationPage.getTotalElements());
        
        // Thêm dữ liệu tìm kiếm vào model để kiểm tra và hiển thị
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        
        return "admin/Donations/index";
    }
    
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model) {
        DonationResponseDto donation = donationsService.findById(id);
        if (donation == null)
            return "redirect:/admin/donations";
        
        // Lấy thông tin chương trình
        ProgramsResponseDto program = programsService.findById(donation.getProgramId());
        
        model.addAttribute("donation", donation);
        model.addAttribute("program", program);
        return "admin/Donations/detail";
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
}
