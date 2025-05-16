package com.example.springdonateweb.Controllers;

import com.example.springdonateweb.Services.interfaces.IDonationsService;
import com.example.springdonateweb.Services.interfaces.IProgramsService;
import com.example.springdonateweb.Services.interfaces.ICategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/charts")
public class ChartController {
    
    private final IDonationsService donationsService;
    private final IProgramsService programsService;
    private final ICategoriesService categoriesService;
    
    @GetMapping("")
    public String showCharts(Model model) {
        return "admin/Charts/index";
    }
    
    @GetMapping("/data/donation-by-program")
    @ResponseBody
    public ResponseEntity<?> getDonationByProgram() {
        try {
            // Get data from service
            Map<String, Double> donationsByProgram = donationsService.getTotalDonationByProgram();
            
            Map<String, Object> response = new HashMap<>();
            response.put("labels", donationsByProgram.keySet());
            response.put("data", donationsByProgram.values());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/data/donations-over-time")
    @ResponseBody
    public ResponseEntity<?> getDonationsOverTime() {
        try {
            // Get data from service - default to last 6 months
            Map<String, Integer> donationsOverTime = donationsService.getDonationCountsByMonth(6);
            
            Map<String, Object> response = new HashMap<>();
            response.put("labels", donationsOverTime.keySet());
            response.put("data", donationsOverTime.values());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/data/donations-by-category")
    @ResponseBody
    public ResponseEntity<?> getDonationsByCategory() {
        try {
            // Get data from service
            Map<String, Double> donationsByCategory = categoriesService.getTotalDonationsByCategory();
            
            Map<String, Object> response = new HashMap<>();
            response.put("labels", donationsByCategory.keySet());
            response.put("data", donationsByCategory.values());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
