package com.example.springdonateweb.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Component
public class AmountFormatter {

    public static String formatAmount(Integer amount) {
        if (amount == null) {
            return "0 đ"; // Handle null values
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(amount);
        return formattedAmount + "đ"; // Append " đ"
    }
}
