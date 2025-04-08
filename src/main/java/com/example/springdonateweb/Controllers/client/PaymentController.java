package com.example.springdonateweb.Controllers.client;

import com.example.springdonateweb.Services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {


    private final VNPayService vnPayService;

    @GetMapping("/{programId}")
    public String home(@PathVariable("programId") int programId, Model model) {
        model.addAttribute("programId", programId);
        return "client/payment";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              @RequestParam("programId") int programId,
                              HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/";
        String vnpayUrl = vnPayService.createOrder(orderTotal, "programId:" + programId + ";userEmail:" + getCurrentUserEmail() + ";orderInfo:" + orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, Model model) throws Exception {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Extract original orderInfo
        String originalOrderInfo = vnPayService.extractOriginalOrderInfo(orderInfo);

        // Format payment time and total price
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(paymentTime, inputFormatter);
        String formattedPaymentTime = dateTime.format(outputFormatter);

        BigDecimal amount = new BigDecimal(totalPrice).divide(new BigDecimal(100));
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = currencyFormatter.format(amount);

        model.addAttribute("orderId", originalOrderInfo);
        model.addAttribute("totalPrice", formattedTotalPrice);
        model.addAttribute("paymentTime", formattedPaymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "client/order-success" : "client/order-fail";
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}