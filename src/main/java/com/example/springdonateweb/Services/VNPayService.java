package com.example.springdonateweb.Services;

import com.example.springdonateweb.Models.Entities.DonationsEntity;
import com.example.springdonateweb.Models.Entities.DonorsEntity;
import com.example.springdonateweb.Models.Entities.ProgramsEntity;
import com.example.springdonateweb.Models.Entities.TransactionsEntity;
import com.example.springdonateweb.Models.Entities.UsersEntity;
import com.example.springdonateweb.Repositories.DonationsRepository;
import com.example.springdonateweb.Repositories.DonorsRepository;
import com.example.springdonateweb.Repositories.ProgramsRepository;
import com.example.springdonateweb.Repositories.TransactionsRepository;
import com.example.springdonateweb.Repositories.UsersRepository;
import com.example.springdonateweb.util.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService {

    @Value("${vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${vnp_HashSecret}")
    private String vnp_HashSecret;

    @Value("${vnp_Url}")
    private String vnp_Url;

    @Value("${vnp_ReturnUrl}")
    private String vnp_ReturnUrl;

    private final VnPayUtil vnPayUtil;
    private final DonationsRepository donationsRepository;
    private final DonorsRepository donorsRepository;
    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;
    private final ProgramsRepository programRepository;

    public String createOrder(int total, String orderInfor, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = vnPayUtil.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

        urlReturn += vnp_ReturnUrl;
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayUtil.hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnp_Url + "?" + queryUrl;
        return paymentUrl;
    }

    public int orderReturn(HttpServletRequest request) throws Exception {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnPayUtil.hashAllFields(fields, vnp_HashSecret);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                // Get payment details from request
                String amount = request.getParameter("vnp_Amount");
                String txnRef = request.getParameter("vnp_TxnRef");
                int programId = extractProgramId(request.getParameter("vnp_OrderInfo"));
                String userEmail = extractUserEmail(request.getParameter("vnp_OrderInfo"));

                // Save payment data
                savePaymentData(amount, txnRef, programId, userEmail);
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    private void savePaymentData(String amount, String transactionId, int programId, String userEmail) {
        try {
            // 1. Create donation record
            UsersEntity user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    
            int donationAmount = new BigDecimal(amount).divide(new BigDecimal(100)).intValue(); // Convert from VNPay amount to int
    
            DonationsEntity donation = DonationsEntity.builder()
                .programId(programId)
                .userId(user.getId())
                .amount(BigDecimal.valueOf(donationAmount)) // Store as BigDecimal in DonationsEntity
                .donationDate(LocalDateTime.now())
                .donorName(user.getName())
                .build();
    
            DonationsEntity savedDonation = donationsRepository.save(donation);
    
            // 2. Create transaction record
            TransactionsEntity transaction = TransactionsEntity.builder()
                .donationId(savedDonation.getDonationId())
                .amount(savedDonation.getAmount())
                .paymentMethodId(1) // VNPay method
                .transactionDate(LocalDateTime.now())
                .status("SUCCESS")
                .build();
    
            transactionsRepository.save(transaction);
    
            // 3. Create donor record
            DonorsEntity donor = DonorsEntity.builder()
                .programId(programId)
                .userId(user.getId())
                .donationId(savedDonation.getDonationId())
                .donationDate(LocalDateTime.now())
                .build();
    
            donorsRepository.save(donor);
    
            // 4. Update program's current amount and donation count
            ProgramsEntity program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));
    
            if (program.getCurrentAmount() == null) {
                program.setCurrentAmount(donationAmount);
            } else {
                program.setCurrentAmount(program.getCurrentAmount() + donationAmount);
            }

            if (program.getDonationCount() == null) {
                program.setDonationCount(1);
            } else {
                program.setDonationCount(program.getDonationCount() + 1);
            }
    
            programRepository.save(program);
    
        } catch (Exception e) {
            throw new RuntimeException("Failed to save payment data", e);
        }
    }

    private int extractProgramId(String orderInfo) {
        // Implement logic to extract program ID from order info
        // Example: orderInfo format could be "programId:123;userEmail:user@example.com"
        String[] parts = orderInfo.split(";");
        return Integer.parseInt(parts[0].split(":")[1]);
    }

    private String extractUserEmail(String orderInfo) {
        // Implement logic to extract user email from order info
        String[] parts = orderInfo.split(";");
        return parts[1].split(":")[1];
    }

    public String extractOriginalOrderInfo(String orderInfo) {
        // Implement logic to extract original order info from order info
        // Example: orderInfo format could be "programId:123;userEmail:user@example.com;orderInfo:Donation for education"
        String[] parts = orderInfo.split(";");
        for (String part : parts) {
            if (part.startsWith("orderInfo:")) {
                return part.substring("orderInfo:".length());
            }
        }
        return "";
    }
}