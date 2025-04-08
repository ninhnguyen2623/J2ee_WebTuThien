package com.example.springdonateweb.Models.Dtos.Users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {



    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]*$", message = "Phone number must be a number")
    public String phoneNumber;

    @NotBlank(message = "Address is required")
    public String address;

}
