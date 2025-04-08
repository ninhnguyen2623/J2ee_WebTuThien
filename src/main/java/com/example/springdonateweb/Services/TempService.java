package com.example.springdonateweb.Services;


import com.example.springdonateweb.Models.Entities.FundCommonEntity;
import com.example.springdonateweb.Repositories.*;
import com.example.springdonateweb.util.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TempService {

    private final VnPayUtil vnPayUtil;
    private final DonationsRepository donationsRepository;
    private final DonorsRepository donorsRepository;
    private final TransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;
    private final FundCommonRepository fundCommonRepository;
    @Value("${vnp_TmnCode}")
    private String vnp_TmnCode;
    @Value("${vnp_HashSecret}")
    private String vnp_HashSecret;
    @Value("${vnp_Url}")
    private String vnp_Url;
    @Value("${vnp_ReturnUrl}")
    private String vnp_ReturnUrl;

    public String createOrder(int total, String orderInfor, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VnPayUtil.getRandomNumber(8);
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
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
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
            fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = VnPayUtil.hashAllFields(fields, vnp_HashSecret);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                // Get payment details from request
                String amount = request.getParameter("vnp_Amount");
                String txnRef = request.getParameter("vnp_TxnRef");

                // Save payment data
                savePaymentData(amount);
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    private void savePaymentData(String amount) {
        try {

            int donationAmount = new BigDecimal(amount).divide(new BigDecimal(100)).intValue(); // Convert from VNPay amount to int


            FundCommonEntity fund = fundCommonRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Fund not found"));

            if (fund.getCurrentAmount() == null) {
                fund.setCurrentAmount(donationAmount);
            } else {
                fund.setCurrentAmount(fund.getCurrentAmount() + donationAmount);
            }

            fundCommonRepository.save(fund);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save payment data", e);
        }
    }

    public String extractOriginalOrderInfo(String orderInfo) {
        String[] parts = orderInfo.split(";");
        for (String part : parts) {
            if (part.startsWith("orderInfo:")) {
                return part.substring("orderInfo:".length());
            }
        }
        return "";
    }
}