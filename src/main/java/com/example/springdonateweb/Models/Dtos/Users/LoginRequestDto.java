package com.example.springdonateweb.Models.Dtos.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto implements Serializable {
    public String email;
    public String password;
}
