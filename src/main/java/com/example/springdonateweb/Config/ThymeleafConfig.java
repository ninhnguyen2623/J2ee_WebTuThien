package com.example.springdonateweb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.springdonateweb.util.AmountFormatter;

@Configuration
public class ThymeleafConfig {

    @Bean
    public AmountFormatter amountFormatter() {
        return new AmountFormatter();
    }
}